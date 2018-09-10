package com.antifake.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Code;

public interface CodeMapper {



	/**
	  * <p>Description: 查找密文</p>
	  * @author Asad 
	  * @date 2018年9月104日
	  */
	Code queryCode(@Param("code")Code code);

	int insertCode(@Param("code")Code code  );
	
	

}
