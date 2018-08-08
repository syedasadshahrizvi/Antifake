package com.antifake.form;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CheckedForm {

	@NotEmpty(message="codeString is empty")
	private String codeString;
	
	/**	姓名*/
	@NotEmpty(message="type is empty")
	private String type;
}
