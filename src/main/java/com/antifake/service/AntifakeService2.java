package com.antifake.service;

import java.util.List;
import java.util.Map;

import com.antifake.model.Antifake;
import com.antifake.model.Cipher;

public interface AntifakeService2 {

	
	List<String> sign(String privateKey,Integer companyId, Integer productId, String template, Integer num) throws Exception;
	
	Map<String, Object> verify(String codeString, String type ) throws Exception;
	
	List<Cipher> listCipher(Antifake antifake,String orderBy,Integer pageNum,Integer pageSize) throws Exception;
	
	void saveSign(Integer companyId, Integer productId, String template, String signature) throws Exception;
}
