package com.antifake.VO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AddressVO {
	
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
	
}
