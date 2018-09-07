package com.antifake.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Antifake;
import com.antifake.model.Cipher;

public interface CipherMapper {

	/**
	  * <p>Description: 插入密文</p>
	  * @author JZR  
	  * @date 2018年5月10日  
	  */
	void insertList(@Param("listCipher")List<Cipher> listCipher);

	/**
	  * <p>Description: 查找密文</p>
	  * @author JZR  
	  * @date 2018年5月10日
	  */
	Cipher queryCipher(@Param("cipher")Cipher cipher);

	/**
	  * <p>Description: 更新查询次数</p>
	  * @author JZR  
	  * @date 2018年5月10日
	  */
	Integer updateCount(@Param("cipher")Cipher cipher);

	/**
	  * <p>Description: 更新密文状态</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Integer updateValid(@Param("cipher")Cipher cipher);

	/**
	  * <p>Description: 按批次更新密文</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Integer updateButchValid(@Param("cipher")Cipher cipher);

	/**
	  * <p>Description: 按序号更改密文</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Integer updateValidByCode(@Param("companyId")Integer companyId, @Param("begain")String begain, @Param("end")String end, @Param("valid")String valid);

	/**
	  * <p>Description: 条件查询密文信息</p>
	  * @author JZR  
	  * @date 2018年5月29日
	  */
	//List<Cipher> listCipher(@Param("companyId")Integer companyId, @Param("companyCode")String companyCode, @Param("productCode")String productCode, @Param("batch")String batch,@Param("orderBy")String orderBy, @Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize);
	List<Cipher> listCipher(@Param("antifake")Antifake antifake,@Param("orderBy")String orderBy, @Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize);
	/**
	  * <p>Description: 查询总量</p>
	  * @author JZR  
	  * @date 2018年5月31日
	  */
	Integer getCount(@Param("batch")String batch);

	/**
	  * <p>Description: 查询用量</p>
	  * @author JZR  
	  * @date 2018年5月31日
	  */
	Integer getUseCount(@Param("batch")String batch);
	
	int insertCode(@Param("productId")Integer productId,@Param("signature")String signature,@Param("codeId")String codeId,@Param("status")Integer status,@Param("date")Date date,@Param("queryTime")Integer queryTime,@Param("companyId")Integer companyId );

}
