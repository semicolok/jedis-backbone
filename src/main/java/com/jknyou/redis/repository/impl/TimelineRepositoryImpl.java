package com.jknyou.redis.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.jknyou.redis.domain.Post;
import com.jknyou.redis.domain.Range;
import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.repository.TimelineRepository;

@Repository
public class TimelineRepositoryImpl implements TimelineRepository {
	@Inject private StringRedisTemplate redisTemplate;
	
	private static final Pattern MENTION_REGEX = Pattern.compile("@[\\w]+");
	
	private final HashMapper<Post, String, String> postMapper = new DecoratingStringHashMapper<Post>(new JacksonHashMapper<Post>(Post.class));

	@Override
	public List<WebPost> getTimelines() {
		return convertPidsToPosts(KeyUtils.timeline(), new Range(0, -1));
	}
	
	private List<WebPost> convertPidsToPosts(String key, Range range) {
		String pid = "pid:*->";
		final String pidKey = "#";
		final String uid = "uid";
		final String content = "content";
		final String replyPid = "replyPid";
		final String replyUid = "replyUid";
		final String time = "time";

		SortQuery<String> query = SortQueryBuilder.sort(key).noSort().get(pidKey).get(pid + uid).get(pid + content).get(
				pid + replyPid).get(pid + replyUid).get(pid + time).limit(range.begin, range.end).build();
		BulkMapper<WebPost, String> hm = new BulkMapper<WebPost, String>() {
			@Override
			public WebPost mapBulk(List<String> bulk) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				Iterator<String> iterator = bulk.iterator();

				String pid = iterator.next();
				map.put(uid, iterator.next());
				map.put(content, iterator.next());
				map.put(replyPid, iterator.next());
				map.put(replyUid, iterator.next());
				map.put(time, iterator.next());

				return convertPost(pid, map);
			}
		};
		List<WebPost> sort = redisTemplate.sort(query, hm);

		return sort;
	}
	
	private WebPost convertPost(String pid, Map<String, String> hash) {
		Post post = postMapper.fromHash(hash);
		WebPost wPost = new WebPost(post);
		wPost.setPid(pid);
		wPost.setName(findName(post.getUid()));
		wPost.setReplyTo(findName(post.getReplyUid()));
		wPost.setContent(replaceReplies(post.getContent()));
		return wPost;
	}
	
	private String replaceReplies(String content) {
		Matcher regexMatcher = MENTION_REGEX.matcher(content);
		while (regexMatcher.find()) {
			String match = regexMatcher.group();
			int start = regexMatcher.start();
			int stop = regexMatcher.end();

			String uName = match.substring(1);
			if (isUserValid(uName)) {
				content = content.substring(0, start) + "<a href=\"!" + uName + "\">" + match + "</a>" + content.substring(stop);
			}
		}
		return content;
	}
	public boolean isUserValid(String name) {
		return redisTemplate.hasKey(KeyUtils.user(name));
	}

	@Override
	public void save(String username, WebPost post) {
		Post p = post.asPost();

		String uid = findUid(username);
		p.setUid(uid);

		String pid = String.valueOf(new RedisAtomicLong(KeyUtils.globalPid(), redisTemplate.getConnectionFactory()).incrementAndGet());

		String replyName = post.getReplyTo();
		if (StringUtils.hasText(replyName)) {
			String mentionUid = findUid(replyName);
			p.setReplyUid(mentionUid);
			// handle mentions below
			p.setReplyPid(post.getReplyPid());
		}

		// add post
		post(pid).putAll(postMapper.toHash(p));

		// add links
		posts(uid).addFirst(pid);
		timeline(uid).addFirst(pid);

		// update followers
		for (String follower : followers(uid)) {
			timeline(follower).addFirst(pid);
		}

		new DefaultRedisList<String>(KeyUtils.timeline(), redisTemplate).addFirst(pid);
		handleMentions(p, pid, replyName);
	}
	private void handleMentions(Post post, String pid, String name) {
		// find mentions
		Collection<String> mentions = findMentions(post.getContent());

		for (String mention : mentions) {
			String uid = findUid(mention);
			if (uid != null) {
				mentions(uid).addFirst(pid);
			}
		}
	}
	private RedisList<String> mentions(String uid) {
		return new DefaultRedisList<String>(KeyUtils.mentions(uid), redisTemplate);
	}
	public static Collection<String> findMentions(String content) {
		Matcher regexMatcher = MENTION_REGEX.matcher(content);
		List<String> mentions = new ArrayList<String>(4);

		while (regexMatcher.find()) {
			mentions.add(regexMatcher.group().substring(1));
		}

		return mentions;
	}
	
	private RedisList<String> timeline(String uid) {
		return new DefaultRedisList<String>(KeyUtils.timeline(uid), redisTemplate);
	}
	private RedisMap<String, String> post(String pid) {
		return new DefaultRedisMap<String, String>(KeyUtils.post(pid), redisTemplate);
	}
	private RedisList<String> posts(String uid) {
		return new DefaultRedisList<String>(KeyUtils.posts(uid), redisTemplate);
	}
	
	
	private String findName(String uid) {
		if (!StringUtils.hasText(uid)) {
			return "";
		}
		BoundHashOperations<String, String, String> userOps = redisTemplate.boundHashOps(KeyUtils.uid(uid));
		return userOps.get("name");
	}
	
	private RedisSet<String> followers(String uid) {
		return new DefaultRedisSet<String>(KeyUtils.followers(uid), redisTemplate);
	}
	public String findUid(String name) {
		return redisTemplate.opsForValue().get(KeyUtils.user(name));
	}

	@Override
	public List<String> getNewUsers() {
		return new DefaultRedisList<String>(KeyUtils.users(), redisTemplate).range(0, -1);
	}

	@Override
	public List<WebPost> getTimeline(String uid) {
		return convertPidsToPosts(KeyUtils.timeline(uid), new Range(0, -1));
	}
}
