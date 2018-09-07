package com.antifake.form;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class AntifakeForm {
	
	private String privateKey;//私钥
	
	private Integer companyId;//公司id
	
	private Integer productId;//产品编码
	
	private String uuid;
	
	private Date date;
	
	private String template;//模板
	
	private Integer count; //生成数量
	
	private Integer cipherId;

	private String signature;
	
	private String publicKey;
	
	private String status; 
	
	private String begain;
	
	private String end;
	
	private String batch;
	
}
