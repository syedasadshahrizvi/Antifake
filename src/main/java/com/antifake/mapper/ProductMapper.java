package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.form.ProductForm;
import com.antifake.form.ProductForm1;
import com.antifake.model.Product;

public interface ProductMapper {

	Integer createProduct(Product product);

	Product queryByCCode(@Param("companyCode")String companyCode);

	Product queryByProductId(@Param("productId")Integer productId);

	List<Product> queryByCId(@Param("companyId")Integer companyId);
	
	
	
	
}
