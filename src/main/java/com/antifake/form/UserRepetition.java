package com.antifake.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserRepetition {
	
	@Pattern(regexp="username|telphone",message="类型格式不正确")
	private String type;
	
	@NotEmpty(message="内容不能为空")
	private String code;
	
}
