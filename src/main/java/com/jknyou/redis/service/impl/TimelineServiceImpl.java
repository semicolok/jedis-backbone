package com.jknyou.redis.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.repository.TimelineRepository;
import com.jknyou.redis.service.TimelineService;

@Service
public class TimelineServiceImpl implements TimelineService {
	@Inject private TimelineRepository timelineRepository;

	@Override
	public List<WebPost> getTimelines() {
		return timelineRepository.getTimelines();
	}

	@Override
	public void postTimeline(String userName, WebPost webpost) {
		timelineRepository.save(userName, webpost);
	}

	@Override
	public List<String> getNewUsers() {
		return timelineRepository.getNewUsers();
	}

	@Override
	public List<String> getFollowers(String uid) {
		return null;
	}

	@Override
	public List<String> getFollowing(String uid) {
		return null;
	}

	@Override
	public List<WebPost> getTimeline(String uid) {
		return timelineRepository.getTimeline(uid);
	}
}
