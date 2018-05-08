package com.antifake.service;

import java.util.List;
import java.util.Map;

import com.antifake.model.Product;

public interface ProductService {

	/**
	  * <p>Description: 创建商品模板</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	Integer createProduct(Integer companyId,String companyCode,String productCode, String template);

	/**
	  * <p>Description: 加密</p>
	  * @author JZR  
	  * @date 2018年4月18日
	  */
	List<String> encrypt(String privateKey, String companyCode, String productCode, String template, Integer count);

	/**
	  * <p>Description: 解密</p>
	  * @author JZR  
	 * @param type 
	  * @date 2018年4月23日
	  */
	Map<String,Object> checkCode(String codeString, String type) throws Exception;

	/**
	  * <p>Description: 查询模板</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	Product getProductBypId(Integer productId);

	/**
	  * <p>Description: 根据公司id查询模板</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	List<Product> getProductList(Integer companyId);

}
