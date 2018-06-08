package com.antifake.service;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletResponse;

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

	/**
	  * <p>Description: 生成图片二维码</p>
	  * @author JZR  
	  * @date 2018年6月5日
	  */
	BufferedImage createImgCode(HttpServletResponse response);

	/**
	  * <p>Description: 校验验证码</p>
	  * @author JZR  
	  * @date 2018年6月5日
	  */
	Boolean checkImgCode(String codeId, String code);

}
