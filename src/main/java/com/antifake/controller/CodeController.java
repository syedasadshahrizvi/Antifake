package com.antifake.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.enums.CodeEnum;
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
	@GetMapping("/send/{tel}")
	public ResultVO sendCode(@PathVariable(name="tel",required = true) String telphone) {
		
		if(!RegExUtils.isPhone(telphone)) {
			log.error("【验证码】手机号格式不正确，telphone = {}", telphone);
			throw new AntiFakeException(ResultEnum.TELPHONE_ERROR.getCode(), ResultEnum.TELPHONE_ERROR.getMessage());
		}
		String flag = codeService.sendCode(telphone);
		if("success".equals(flag))
			return ResultVOUtil.success(flag);
		return ResultVOUtil.error(ResultEnum.TEL_CODE_ERROR.getCode(), ResultEnum.TEL_CODE_ERROR.getMessage());
	}
	
	/**
	 * <p>Description: 校验验证码</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	@GetMapping("/check/{tel}/{code}")
	public ResultVO checkCode(@PathVariable(name="tel",required = true) String telphone,@PathVariable(name="code",required = true) String code) {
		if(codeService.checkCode(telphone,code))
			return ResultVOUtil.success();
		return ResultVOUtil.error(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
	}
	
	/**
	  * <p>Description: 生成图片验证码</p>
	  * @author JZR  
	  * @date 2018年6月5日
	  */
	@GetMapping("/create/img/code")
	public void createImgCode(HttpServletResponse response) throws Exception{
		BufferedImage image = codeService.createImgCode(response);
        response.setContentType("image/png");    
        OutputStream os = response.getOutputStream();    
        ImageIO.write(image, "png", os); 
	}
	
	/**
	  * <p>Description: 校验图形验证码</p>
	  * @author JZR  
	  * @date 2018年6月5日
	  */
	@GetMapping("/checkcode/{code}")
	public ResultVO checkImgCode(HttpServletRequest request,@PathVariable(name="code",required = true) String code) {
		String codeId = request.getHeader(CodeEnum.CODE_ID.getCode());
		if(codeId==null) {
			log.error("【校验验证码】验证码ID不存在！");
			throw new AntiFakeException(ResultEnum.CODEID_ERROR);
		}
		Boolean b = codeService.checkImgCode(codeId,code);
		return ResultVOUtil.success(b);
	}
	
}
