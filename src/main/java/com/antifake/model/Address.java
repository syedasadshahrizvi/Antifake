package com.antifake.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="account_user_address")
@EntityListeners(AuditingEntityListener.class)
public class Address {
	
	/**	地址id*/
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer addressId;
	
	/**	用户id*/
	private String userId;
	
	/**	姓名*/
	private String name;
	
	/**	手机号*/
	private String telphone;
	
	/**	省份*/
	private String province;
	
	/**	城市*/
	private String city;
	
	/**	区县*/
	private String county;
	
	/**	街道镇*/
	private String community;
	
	/**	详细地址*/
	private String address;
	
	/**	邮编*/
	private String postCode;
	
	/**	是否是默认地址*/
	private Integer isdefault;
	
	/**	地址状态*/
	private Integer	status;
	
	/**	创建时间*/
	@CreatedDate
	private Date	createTime;

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", userId=" + userId + ", name=" + name + ", telphone=" + telphone
				+ ", province=" + province + ", city=" + city + ", county=" + county + ", community=" + community
				+ ", address=" + address + ", postCode=" + postCode + ", isdefault=" + isdefault + ", status=" + status
				+ ", createTime=" + createTime + "]";
	}
	
}
