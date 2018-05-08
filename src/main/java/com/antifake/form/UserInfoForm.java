package com.antifake.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserInfoForm {
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String nickname;
}
