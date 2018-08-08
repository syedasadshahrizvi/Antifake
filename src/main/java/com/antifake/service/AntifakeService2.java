package com.antifake.service;

import java.util.List;
import java.util.Map;

public interface AntifakeService2 {

	
	List<String> sign(String privateKey,Integer companyId, Integer productId, String template) throws Exception;
	
	Map<String, Object> verify(String codeString, String type ) throws Exception;
}
