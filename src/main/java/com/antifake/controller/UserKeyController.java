package com.antifake.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.VO.UserVO;
import com.antifake.enums.ResultEnum;
import com.antifake.model.PubKey;
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
	
	/**
	  * <p>Description: 获取用户公钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	@GetMapping("/get/{uid}")
	public ResultVO<List<PubKey>> getPubKey(@PathVariable("uid")String uid){
		List<PubKey> pubKeylist = keyService.getPubKey(uid);
		return ResultVOUtil.success(pubKeylist);
	}
	
	/**
	  * <p>Description: 根据公钥查询用户</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	@PostMapping("/get/user")
	public ResultVO<UserVO>  getUser(@RequestParam(name="pubKey",required=true)String pubKey){
		UserVO userVO = keyService.getUser(pubKey);
		return ResultVOUtil.success(userVO);
	}
	
	/**
	  * <p>Description: 替换秘钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	@PostMapping("/replace")
	public ResultVO	replace(@RequestParam(name="oldPubKey",required=true)String oldPubKey,@RequestParam(name="newPubKey",required=true)String newPubKey) {
		keyService.replacePubKey(oldPubKey,newPubKey);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 删除秘钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	@PostMapping("/delete")
	public ResultVO	delete(@RequestParam(name="pubKey",required=true)String pubKey) {
		keyService.delete(pubKey);
		return ResultVOUtil.success();
	}
}
