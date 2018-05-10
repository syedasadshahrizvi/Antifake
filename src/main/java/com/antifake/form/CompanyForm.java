package com.antifake.form;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CompanyForm {
	
	@NotBlank(message="公司id不能为空")
	private Integer companyId;
	
	@NotEmpty(message="公司名不能为空")
	private String companyname;
	
	@NotEmpty(message="用户Id不能为空")
	private String userId;
	
	@NotEmpty(message="编号不能为空")
	private String registerId;
	
	//@NotBlank(message="公司级别格式不正确")
	private Byte level;
	
	private String BusinessLicense;
}
