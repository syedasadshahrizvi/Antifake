package com.antifake.service.impl;

import io.github.pixee.security.Newlines;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.enums.CodeEnum;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.service.CodeService;
import com.antifake.utils.SendSmsUtil;
import com.antifake.utils.UUIDUtil;
import com.antifake.utils.VerifyUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
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

	@Override
	public BufferedImage createImgCode(HttpServletResponse response) {
		//利用图片工具生成图片    
        //第一个参数是生成的验证码，第二个参数是生成的图片    
        Object[] objs = VerifyUtil.createImage();    
        //将验证码存入缓存中   
        String codeId = CodeEnum.CODE_ID.getCode() + "_" + UUIDUtil.get32UUID();
        redisTemplate.opsForValue().set(codeId, objs[0].toString(), 30, TimeUnit.MINUTES);
        response.setHeader(CodeEnum.CODE_ID.getCode(), Newlines.stripAll(codeId));
        //将图片输出给浏览器    
        BufferedImage image = (BufferedImage) objs[1]; 
		return image;
	}

	@Override
	public Boolean checkImgCode(String codeId, String code) {
		String oldCode = redisTemplate.opsForValue().get(codeId);
		if(oldCode == null) {
			log.error("【校验验证码】验证码为空, codeId = {}", codeId);
			throw new AntiFakeException(ResultEnum.CODE_ERROR);
		}
		if(oldCode.equalsIgnoreCase(code)) {
			return true;
		}
		return false;
	}

}
