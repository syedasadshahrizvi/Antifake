package com.antifake.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Code;

public interface AntifakeService2 {

	
	List<String> sign(String privateKey,Integer companyId, Integer productId, String template, Integer num) throws Exception;
	
	Map<String, Object> verify(String codeString, String type ) throws Exception;
	
	Map<String, Object> verifyCode(String codeString, String signature ) throws Exception;
	
	List<Cipher> listCipher(Antifake antifake,String orderBy,Integer pageNum,Integer pageSize) throws Exception;
	
	void saveSign(Integer companyId, Integer productId, String template, String signature) throws Exception;
	
	Boolean postCode(Code code) throws Exception;
	
	public void  updateCode() throws Exception;

	Map<String, Object> getCode(String codeString) throws Exception;
	Boolean verifyToken(String token, Integer CompanyId) throws Exception;
}
