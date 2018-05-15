package com.antifake.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserRegistForm {
	
	/**	账户.	*/
	@Pattern(regexp="^[a-zA-Z][a-zA-Z0-9_]{3,15}$",message="用户名必须为4到18位的字母或数字")
	private String username;
	
	/** 密码.	*/
	@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$", message="密码必须是6~16位数字和字母的组合")
	private String password;
	
	/**	手机号.*/
	@Pattern(regexp="^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message="手机号格式不正确")
	private String telphone;
	
	/**	验证码.*/
	@NotEmpty(message="验证码不能为空！")
	private String code;

}
