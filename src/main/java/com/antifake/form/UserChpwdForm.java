package com.antifake.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserChpwdForm {
	
	@NotEmpty
	private String userId;
	
	@NotEmpty
	private String oldpwd;
	
	@NotEmpty
	private String newpwd;
}
