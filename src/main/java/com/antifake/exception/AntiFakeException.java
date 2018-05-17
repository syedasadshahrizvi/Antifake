package com.antifake.exception;

import com.antifake.enums.ResultEnum;

import lombok.Data;

@Data
public class AntiFakeException extends RuntimeException{
	
	 private Integer code;

	    public AntiFakeException(ResultEnum resultEnum) {
	        super(resultEnum.getMessage());

	        this.code = resultEnum.getCode();
	    }

	    public AntiFakeException(Integer code, String message) {
	        super(message);
	        this.code = code;
	    }

}
