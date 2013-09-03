package com.jknyou.redis.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.service.TimelineService;

@Controller
public class TimeLineController {
	
	@Inject private TimelineService timelineService;
	
	private static final String JSON_VIEW = "jsonView";
	
	@RequestMapping(value = "/datas", method = RequestMethod.GET)
	public String getDatas(ModelMap map){
		map.put("timelines", timelineService.getTimelines());
		map.put("newUsers", timelineService.getNewUsers());
		return JSON_VIEW;
	}
	
	@RequestMapping(value = "/timelines", method = RequestMethod.GET)
	public String getAllTimelines(ModelMap map){
		map.put("timelines", timelineService.getTimelines());
		return JSON_VIEW;
	}
	
	@RequestMapping(value = "/timelines/{uid}", method = RequestMethod.GET)
	public String getTimeline(@PathVariable String uid, ModelMap map){
		map.put("timelines", timelineService.getTimeline(uid));
		return JSON_VIEW;
	}
	
	@RequestMapping(value = "/timelines", method = RequestMethod.POST)
	public String addTimeline(@RequestBody WebPost webpost, ModelMap map){
		System.out.println(webpost);
		timelineService.postTimeline("jknyou", webpost);
//		map.put("timelines", timelineService.getTimelines());
		return JSON_VIEW;
	}
	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public String getFollowers(ModelMap map){
		map.put("followers", timelineService.getFollowers("uid"));
		return JSON_VIEW;
	}
	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public String getFollowing(ModelMap map){
		map.put("following", timelineService.getFollowing("uid"));
		return JSON_VIEW;
	}
	
}
