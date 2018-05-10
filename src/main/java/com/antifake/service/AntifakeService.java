package com.antifake.service;

import java.util.List;
import java.util.Map;

public interface AntifakeService {
	
	/**
	  * <p>Description: 加密</p>
	  * @author JZR  
	  * @date 2018年4月18日
	  */
	List<String> encrypt(String privateKey,Integer companyId, String companyCode, String productCode, String template, Integer count);

	/**
	  * <p>Description: 解密</p>
	  * @author JZR  
	 * @param type 
	  * @date 2018年4月23日
	  */
	Map<String,Object> checkCode(String codeString, String type) throws Exception;
}
