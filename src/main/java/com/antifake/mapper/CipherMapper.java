package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Cipher;
import com.antifake.model.Expre;

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

}
