package com.antifake.service;

import java.util.List;

import com.antifake.model.ProductProperty;

public interface ProductPropertyService {

	/**
	  * <p>Description: 添加</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	ProductProperty add(ProductProperty converter);

	/**
	  * <p>Description: 编辑</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	ProductProperty edit(ProductProperty converter);

	/**
	  * <p>Description: 查询全部</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	List<ProductProperty> findAll();

	/**
	  * <p>Description: 删除</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	ProductProperty delete(Integer pid);

	/**
	  * <p>Description: 根据id查询</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	ProductProperty getById(Integer pid);
	
}
