package com.antifake.form;

import lombok.Data;

@Data
public class AntifakeFrom {
	
	private String privateKey;//私钥
	
	private Integer companyId;//公司id
	
	private String companyCode;//公司编码
	
	private String productCode;//产品编码
	
	private String template;//模板
	
	private Integer count; //生成数量
	
}
