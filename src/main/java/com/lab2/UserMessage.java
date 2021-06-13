package com.lab2;

public class UserMessage {
	private UserMessageType type;
	private String username;
	public enum UserMessageType {
        JOIN, LEAVE
    }
	public UserMessageType getType() {
		return type;
	}
	public void setType(UserMessageType type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
