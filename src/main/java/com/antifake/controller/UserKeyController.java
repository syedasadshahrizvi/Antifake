package com.antifake.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.enums.ResultEnum;
import com.antifake.service.CodeService;
import com.antifake.service.KeyService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/key")
@Slf4j
public class UserKeyController {
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private KeyService keyService;
	
	/**
	 * <p>
	 * Description: 生成密钥
	 * </p>
	 * 
	 * @author JZR
	 * @throws Exception 
	 * @date 2018年4月17日
	 */
	@PostMapping("/createkey")
	public ResultVO<Map<String, String>> createKey(String userId, String telphone, String code) throws Exception {
		// 判断验证码
		if (!codeService.checkCode(telphone, code))
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		String privateKey = keyService.createKey(userId);
		log.info("【密钥】生成密钥,userId={}",userId);
		return ResultVOUtil.success(privateKey);
	}
}
