package com.antifake.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserChpwdByTelForm {
	
	@NotEmpty
	private String userId;
	
	@NotEmpty
	private String telphone;
	
	@NotEmpty
	private String code;
	
	@NotEmpty
	private String newpwd;
	
}
