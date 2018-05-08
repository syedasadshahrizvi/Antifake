package com.antifake.model;

public class PrivateKey {
	
	private String userId;
	
	private String privateKey;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public String toString() {
		return "PrivateKey [userId=" + userId + ", privateKey=" + privateKey + "]";
	}
	
}
