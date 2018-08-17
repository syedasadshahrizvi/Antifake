package com.antifake.service;

import java.util.List;
import java.util.Map;

import com.antifake.form.ProductForm1;
import com.antifake.model.Product;

public interface ProductService {

	/**
	  * <p>Description: 创建商品模板</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	Integer createProduct(Product product);


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
	List<ProductForm1> getProductList1(Integer companyId);
}
