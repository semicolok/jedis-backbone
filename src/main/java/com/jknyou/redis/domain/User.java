package com.jknyou.redis.domain;


public class User {

	private Long id;
	private String name;
	private String pass;
	private String authKey;

	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String password) {
		this.pass = password;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String loginKey) {
		this.authKey = loginKey;
	}
}