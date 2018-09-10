package com.antifake.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="company_product")

public class CompanyProduct {

	@Id
    private Integer productId;
	
	/**	Companyid*/
	private Integer companyId;
	
	/**	公钥*/
	private String publicKey;
	
	/**	公钥状态0、删除1、正常*/
	
	
}




