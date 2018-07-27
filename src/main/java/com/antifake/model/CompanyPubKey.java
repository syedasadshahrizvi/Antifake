package com.antifake.model;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="company_pub_keys")
public class CompanyPubKey {
	
	@Id
    private Integer id;
	
	/**	Companyid*/
	private Integer companyId;
	
	/**	公钥*/
	private String publicKey;
	
	/**	公钥状态0、删除1、正常*/
	public Integer status = 0;
	
	

}
