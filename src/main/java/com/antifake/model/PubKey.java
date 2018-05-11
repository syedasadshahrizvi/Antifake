package com.antifake.model;

public class PubKey {
	
	private Integer id;
	
	private String userId;
	
	private String publicKey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "UserKey [id=" + id + ", userId=" + userId + ", publicKey=" + publicKey + "]";
	}
	
	
}
