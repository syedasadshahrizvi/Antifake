package com.antifake.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {
	CODE_ID("code_id","图片验证码id"),
	;
	
	private String code;
	
	private String msg;

	private CodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
}
