package com.antifake.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.antifake.VO.ResultVO;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandle {
		
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResultVO	handle(Exception e) {
		if(e instanceof AntiFakeException) {
			AntiFakeException antiFakeException = (AntiFakeException) e;
			return ResultVOUtil.error(antiFakeException.getCode(), antiFakeException.getMessage());
		} else {
			log.error("【系统异常】 {}", e);
			return ResultVOUtil.error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMessage());
		}
	}
	
}
