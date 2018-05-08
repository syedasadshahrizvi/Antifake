package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Cipher;

public interface CipherMapper {

	void insertList(@Param("listCipher")List<Cipher> listCipher);

	Cipher queryCipher(@Param("cipher")Cipher cipher);

}
