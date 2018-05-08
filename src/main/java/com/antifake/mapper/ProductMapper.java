package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Product;

public interface ProductMapper {

	Integer createProduct(@Param("companyId")Integer companyId,@Param("companyCode")String companyCode,@Param("productCode")String productCode, @Param("template")String template);

	Product queryByCCode(@Param("companyCode")String companyCode);

	Product queryByProductId(@Param("productId")Integer productId);

	List<Product> queryByCId(@Param("companyId")Integer companyId);
	
}
