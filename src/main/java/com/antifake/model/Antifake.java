package com.antifake.model;
import org.springframework.web.bind.annotation.RequestParam;

import com.antifake.form.AntifakeForm;

import lombok.Data;

@Data
public class Antifake {

	private String privateKey;//私钥
	
	private Integer companyId;//公司id
	
	private String companyCode;//公司编码
	
	private String productCode;//产品编码
	
	private String template;//模板
	
	private Integer count; //生成数量
	
	private Integer cipherId;
	
	private String status;
	
	private String begain;
	
	private String end;
	
	private String batch;
	
}
