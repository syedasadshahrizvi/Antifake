package com.antifake.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserInfoForm {
	
	@NotEmpty(message="用戶id不能为空")
	private String userId;
	
	@NotEmpty(message="昵称不能为空")
	private String nickname;
}
