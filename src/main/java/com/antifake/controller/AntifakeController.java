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
import com.antifake.service.AntifakeService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/antifake")
@Slf4j
public class AntifakeController {
	
	@Autowired
	private AntifakeService antifakeService;
	
	/**
	  * <p>Description: 生成防伪码</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@PostMapping("/create")
	public ResultVO<List<String>> createCipher(String privateKey,Integer companyId,String companyCode,String productCode,String template,Integer count) {
		List<String> list = antifakeService.encrypt(privateKey,companyId,companyCode,productCode,template,count);
		return ResultVOUtil.success(list);
	}
	
	/**
	  * <p>Description: 校验防伪码</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/checked/{codeString}/{type}")
	public ResultVO<Map<String,Object>> checkCipher(@PathVariable("codeString")String codeString,@PathVariable(name="type")String type) throws Exception {
		Map<String,Object> resultMap = antifakeService.checkCode(codeString,type);
		return ResultVOUtil.success(resultMap);
	}
	
	//统计防伪信息
	
}
