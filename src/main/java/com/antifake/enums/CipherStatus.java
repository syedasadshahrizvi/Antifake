package com.antifake.enums;

import lombok.Getter;

@Getter
public enum CipherStatus {
	
	UP("0","启用"),
	DOWN("1","禁用"),
	BACK("2","召回")
	;
	
	private String code;
	
	private String mesg;

	private CipherStatus(String code, String mesg) {
		this.code = code;
		this.mesg = mesg;
	}
	
}
