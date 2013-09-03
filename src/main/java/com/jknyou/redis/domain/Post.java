package com.jknyou.redis.domain;


public class Post {

	private String content;
	private String uid;
	private String time = String.valueOf(System.currentTimeMillis());
	private String replyPid;
	private String replyUid;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String author) {
		this.uid = author;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getReplyPid() {
		return replyPid;
	}
	public void setReplyPid(String replyPid) {
		this.replyPid = replyPid;
	}
	public String getReplyUid() {
		return replyUid;
	}
	public void setReplyUid(String replyUid) {
		this.replyUid = replyUid;
	}

	@Override
	public String toString() {
		return "Post [content=" + content + ", replyPid=" + replyPid + ", replyUid=" + replyUid
				+ ", time=" + time + ", uid=" + uid + "]";
	}
}