package com.antifake.mapper;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Expre;

public interface ExpreMapper {

	/**
	  * <p>Description: 插入当前批次明文</p>
	  * @author JZR  
	  * @date 2018年5月10日
	  */
	Integer insertExpre(@Param("expre")Expre expre);

	/**
	  * <p>Description: 根据公司id和批次查询明文</p>
	  * @author JZR  
	  * @date 2018年5月10日
	  */
	Expre queryExpreByCId(@Param("companyId")Integer companyId,@Param("batch")String batch);


}
