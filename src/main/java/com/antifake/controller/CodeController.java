package com.antifake.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.service.CodeService;
import com.antifake.utils.RegExUtils;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/code")
@Slf4j
public class CodeController {
	
	@Autowired
	private CodeService codeService;
	
	/**
	 * <p>Description: 发送验证码</p>
	 * @author JZR  
	 * @date 2018年4月10日 
	 */
	@GetMapping("/sendcode/{tel}")
	public ResultVO sendCode(@PathVariable(name="tel",required = true) String telphone) {
		
		if(!RegExUtils.isPhone(telphone)) {
			log.error("【验证码】手机号格式不正确，telphone = {}", telphone);
			throw new AntiFakeException(ResultEnum.TELPHONE_ERROR.getCode(), ResultEnum.TELPHONE_ERROR.getMessage());
		}
		String flag = codeService.sendCode(telphone);
		return ResultVOUtil.success(flag);
	}
	
	/**
	 * <p>Description: 校验验证码</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	@GetMapping("/checkcode/{tel}/{code}")
	public ResultVO checkCode(@PathVariable(name="tel",required = true) String telphone,@PathVariable(name="code",required = true) String code) {
		if(codeService.checkCode(telphone,code))
			return ResultVOUtil.success();
		return ResultVOUtil.error(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
	}
	
}
