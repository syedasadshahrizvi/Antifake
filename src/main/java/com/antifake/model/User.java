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

    private Integer sex;

    private Integer realStatus;

    private Integer status;
    
    private List<PubKey> userKeyList;
    
    private List<Role> roleList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getRealStatus() {
		return realStatus;
	}

	public void setRealStatus(Integer realStatus) {
		this.realStatus = realStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<PubKey> getUserKeyList() {
		return userKeyList;
	}

	public void setUserKeyList(List<PubKey> userKeyList) {
		this.userKeyList = userKeyList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", nickname=" + nickname
				+ ", telphone=" + telphone + ", idCard=" + idCard + ", realname=" + realname + ", sex=" + sex
				+ ", realStatus=" + realStatus + ", status=" + status + ", userKeyList=" + userKeyList + ", roleList="
				+ roleList + "]";
	}
    
}