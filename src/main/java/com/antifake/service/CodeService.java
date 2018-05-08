package com.antifake.service;

public interface CodeService {

	/**
	 * <p>Description: 发送验证码</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	String sendCode(String telphone);

	/**
	 * <p>Description: 校验验证码</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	Boolean checkCode(String telphone, String code);

}
