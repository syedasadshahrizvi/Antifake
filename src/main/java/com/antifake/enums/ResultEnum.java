package com.antifake.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
	
	ERROR(-1,"未知错误！"),
	SECCESS(0, "成功"),
	PARAM_ERROR(1, "参数不正确！"),
	USERNAME_IS_EXIST(2,"账户或手机号已存在！"),
	TELPHONE_IS_EXIST(3,"手机号已被注册！"),
	LOGIN_ERROE(4,"用户名或密码错误！"),
	TELPHONE_ERROR(5,"手机号格式不正确！"),
	CODE_ERROR(6,"验证码有误！"),
	OLDPWD_ERROR(7,"密码错误！"),
	COMPANY_REPEAT(8,"公司已注册！"),
	IMG_TYPE_ERROR(9,"图片类型不正确！"),
	COMPANY_ERROR(10,"公司尚未审核通过！"),
	LOGIN_NULL(11,"用户尚未登陆！"),
	TEL_CODE_ERROR(13,"验证码发送失败，请稍后再试！"),
	ID_ERROR(14,"Id不能为空！"),
	USER_NOT_EXIST(15,"用户不存在！"),
	PUB_KEY_NOT_EXIST(16,"公钥不存在！!"),
	AUTHO_ERROR(401,"没有权限访问！"),
	F0F(404,"找不到页面！");
	
	private Integer code;
	
	private String message;
	
	ResultEnum(Integer code,String message){
		this.code = code;
		this.message = message;
	}
	
}
