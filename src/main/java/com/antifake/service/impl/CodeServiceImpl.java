package com.antifake.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.antifake.service.CodeService;
import com.antifake.utils.SendSmsUtil;

@Service
public class CodeServiceImpl implements CodeService{
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public String sendCode(String telphone) {
		//生成验证码
		String captcha = SendSmsUtil.getCaptcha();
		//发送验证码
		String templateParam = "{\"code\":\"" + captcha + "\"}";
		String status = SendSmsUtil.sendCodeSms(telphone, templateParam);
		if(status.equals("success")) {
			//发送成功保存验证码
			redisTemplate.opsForValue().set("code_"+telphone, captcha,30,TimeUnit.MINUTES);
		}
		return status;
	}

	@Override
	public Boolean checkCode(String telphone, String code) {
		String realcode = redisTemplate.opsForValue().get("code_"+telphone);
		return code.equals(realcode);
	}

}
