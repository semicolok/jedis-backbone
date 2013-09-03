package com.jknyou.redis.service;

import java.util.List;

import com.jknyou.redis.domain.WebPost;

public interface TimelineService {
	List<WebPost> getTimelines();
	void postTimeline(String userName, WebPost webpost);
	List<String> getNewUsers();
	List<String> getFollowers(String uid);
	List<String>  getFollowing(String uid);
	List<WebPost> getTimeline(String uid);
}
