package com.antifake.service;

import java.util.List;
import java.util.Map;

import com.antifake.VO.ExpreVO;
import com.antifake.model.Cipher;

public interface AntifakeService {
	
	/**
	  * <p>Description: 加密</p>
	  * @author JZR  
	  * @date 2018年4月18日
	  */
	List<String> encrypt(String privateKey,Integer companyId, String companyCode, String productCode, String template, Integer count);

	/**
	  * <p>Description: 解密</p>
	  * @author JZR  
	 * @param type 
	  * @date 2018年4月23日
	  */
	Map<String,Object> checkCode(String codeString, String type) throws Exception;

	/**
	  * <p>Description: 单个作废/召回</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Cipher updateCipher(Integer cipherId, Integer companyId,String privateKey,String status) throws Exception;

	/**
	  * <p>Description: 按批次作废/召回</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Integer updateButchCipher(Integer companyId, String batch, String privateKey,String status) throws Exception;

	/**
	  * <p>Description: 按序号作废/召回</p>
	  * @author JZR  
	  * @date 2018年5月28日
	  */
	Integer updateCodeCipher(Integer companyId, String begain, String end, String privateKey,String status) throws Exception;

	/**
	  * <p>Description: 查询统计</p>
	  * @author JZR  
	  * @date 2018年5月29日
	  */
	List<Cipher> listCipher(Integer companyId, String companyCode, String productCode, String batch,
			String orderBy, Integer pageNum, Integer pageSize) throws Exception;

	/**
	  * <p>Description: 查询明文</p>
	  * @author JZR  
	  * @date 2018年5月30日
	  */
	List<ExpreVO> listExpre(Integer companyId,Integer pageNum,Integer pageSize);

}
