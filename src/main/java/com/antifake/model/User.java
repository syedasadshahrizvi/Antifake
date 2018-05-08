package com.antifake.model;

import java.util.List;

public class User {
    private String userId;

    private String username;

    private String password;

    private String nickname;

    private String telphone;

    private String idCard;

    private String realname;

    private Byte sex;

    private Byte realStatus;

    private Byte status;
    
    private List<UserKey> userKeyList;
    

    public List<UserKey> getUserKeyList() {
		return userKeyList;
	}

	public void setUserKeyList(List<UserKey> userKeyList) {
		this.userKeyList = userKeyList;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Byte getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(Byte realStatus) {
        this.realStatus = realStatus;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", nickname=" + nickname
				+ ", telphone=" + telphone + ", idCard=" + idCard + ", realname=" + realname + ", sex=" + sex
				+ ", realStatus=" + realStatus + ", status=" + status + ", userKeyList=" + userKeyList + "]";
	}
    
    
}