package com.jknyou.redis.repository;

import java.util.List;

import com.jknyou.redis.domain.WebPost;

public interface TimelineRepository {
	List<WebPost> getTimelines();
	void save(String userName, WebPost webpost);
	List<String> getNewUsers();
	List<WebPost> getTimeline(String uid);

}
