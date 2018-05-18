package com.antifake.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ProductPropertyForm {
	
	/**	id*/
	private Integer propertyId;
	
	/**	编码*/
	@NotEmpty(message="编码不能为空！")
	private String code;
	
	/**	名称*/
	@NotEmpty(message="名称不能为空！")
	private String name;
	
	/**	类型*/
	@NotEmpty(message="类型不能为空！")
	private String type;
	
}
