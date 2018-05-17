package com.antifake.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class AddressForm {
	
	/**	地址Id*/
	private Integer	addressId;
	
	/**	用户id*/
	@NotEmpty(message="用户id不能为空")
	private String userId;
	
	/**	姓名*/
	@NotEmpty(message="姓名不能为空")
	private String name;
	
	/**	手机号*/
	@NotEmpty(message="手机号不能为空")
	private String telphone;
	
	/**	省份*/
	@NotEmpty(message="省份信息不能为空")
	private String province;
	
	/**	城市*/
	@NotEmpty(message="所在城市不能为空")
	private String city;
	
	/**	区县*/
	@NotEmpty(message="地区不能为空")
	private String county;
	
	/**	街道镇*/
	private String community;
	
	/**	详细地址*/
	@NotEmpty(message="详细地址不能为空")
	private String address;
	
	/**	邮编*/
	private String postCode;
	
	/**	是否是默认地址*/
	
	private Integer isdefault;
	
}
