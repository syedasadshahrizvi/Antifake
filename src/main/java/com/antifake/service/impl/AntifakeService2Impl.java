package com.antifake.service.impl;




import java.io.File;
import java.nio.charset.Charset;
import java.security.interfaces.ECPrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.alibaba.druid.util.HexBin;
import com.antifake.mapper.ExpreMapper;
import com.antifake.mapper.ProductMapper;
import com.antifake.enums.CipherStatus;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CipherMapper;
import com.antifake.mapper.CodeMapper;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.CompanyProductRepository;
import com.antifake.mapper.CompanyPubKeyRepository;
import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Code;
import com.antifake.model.Company;
import com.antifake.model.CompanyProduct;
import com.antifake.model.CompanyPubKey;
import com.antifake.model.Expre;
import com.antifake.model.Product;
import com.antifake.service.AntifakeService2;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.ECCUtil2;
import com.antifake.utils.ECCUtil4;
import com.antifake.utils.FileUploadUtil;
import com.antifake.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class AntifakeService2Impl implements AntifakeService2 {
	
	public static final Integer STATUS = 0;
	public static final String checkCount = "0";
	public static final String requestType = "S1S";
	
	@Autowired
	private ExpreMapper expreMapper;
	@Autowired
	private CipherMapper cipherMapper;
	
	@Autowired
	private StringRedisTemplate redisTemplate;


	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private CodeMapper codeMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	
	@Autowired
	private CompanyPubKeyRepository companyPubKeyRepository;
	
	@Autowired
	//private CompanyProductRepository companyProductRepository;
	

	
	@Override
	// 待优化
	public List<String> sign(String privateKey, Integer companyId, Integer productId,
			String template,Integer num) throws Exception {
		  
		
		long start = System.currentTimeMillis();
		
		List<Cipher> listCipher = new ArrayList<Cipher>();
		List<String> listString = new ArrayList<String>();	
			
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		
		for (Integer i = 0; i < num; i++) 
		
		{
			
				Long increment = redisTemplate.opsForValue().increment("" + companyId + "_" + productId, 1L);
				if (increment > 999999999) {
					redisTemplate.opsForValue().set("" + companyId + "_" + productId, "1");
				}
				
				String get12uuid = UUIDUtil.get12UUID();
				String templates = template + "." + get12uuid;
				String signature =ECCUtil2.sign(templates, privateKey);
				

				Cipher cipher = new Cipher();
				//System.out.println(signature);
				//System.out.println("length" + signature.length());
					
				String substringBegin = signature.substring(0, 10);
				String substringLast = StringUtils.substring(signature, 10);
				String stringCode = "" + companyId + "." + productId + "." + substringBegin + "." + increment ;
				cipher.setCompanyId(companyId);
				cipher.setProductId(productId);
				cipher.setCipherText(substringLast);
				cipher.setBatch(batch);
				cipher.setRanKey(get12uuid);
				
				
				cipher.setCode("" + increment);
					
				listCipher.add(cipher);
				listString.add(stringCode);
		
		}
		
		long end = System.currentTimeMillis();
		System.out.println("数据处理耗时："+(end-start));
		// 存储加密信息
		long start2 = System.currentTimeMillis();
		cipherMapper.insertList(listCipher);
		System.err.println("存储耗时 ：" + (System.currentTimeMillis() - start2) + "毫秒");
		
		
		return listString;
    	
    	
    	//Boolean bool= ECCUtil2.verify(template, signature, "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEibHtxDEbBrytInM7s6YZyHmOSeiK/GUpKB6JQRZotPZcnbtfgKQhLZsebaT7kWl1Pe6T5TCgSI32elW8HVycdw==");
    	//System.out.println(bool);
		
	}
	
	public void saveSign(Integer companyId, Integer productId,
			String template,String signature) throws Exception
	{
		  
		List<Cipher> listCipher = new ArrayList<Cipher>();
		
			
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		
		
		Long increment = redisTemplate.opsForValue().increment("" + companyId + "_" + productId, 1L);
		if (increment > 999999999)
		{
			redisTemplate.opsForValue().set("" + companyId + "_" + productId, "1");
		}
		
		
		Cipher cipher = new Cipher();
		cipher.setCompanyId(companyId);
		cipher.setProductId(productId);
		cipher.setCipherText(signature);
		cipher.setBatch(batch);
		cipher.setCode("" + increment);	
		listCipher.add(cipher);
		cipherMapper.insertList(listCipher);
		
	
	}
	
	public Boolean postCode(Code code) throws Exception
	{
		
				  
		productMapper.updateProductTime(code.getProductId());
		
		codeMapper.insertCode(code);
		
		
		return true;
		
	
	}
	
	
	
	
	
	
	public Map<String, Object> verify(String codeString, String type) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<>();
		String[] split = StringUtils.split(codeString, ".");
		String companyId = split[0];
		String productId = split[1];
		String cipherText = split[2];
		String increment = split[3];
		Cipher cipher = new Cipher();
		cipher.setCompanyId(Integer.parseInt(companyId));
		cipher.setProductId(Integer.parseInt(productId));
		cipher.setCode(increment);
		Cipher resultCipher = cipherMapper.queryCipher(cipher);
		Expre resultCheck = expreMapper.queryExpreByCId(resultCipher.getCompanyId(), resultCipher.getBatch());
		// 校验
		// 查询公钥
		Company company = companyMapper.selectByPrimaryKey(resultCipher.getCompanyId());
		
		CompanyPubKey companyPubKey = new CompanyPubKey();
		companyPubKey.setCompanyId((Integer)company.getCompanyId());
		companyPubKey.setStatus(STATUS);
		
		Example<CompanyPubKey> example = Example.of(companyPubKey);
		List<CompanyPubKey> userKeyList = companyPubKeyRepository.findAll(example);
		
		
		
		cipherText = cipherText + resultCipher.getCipherText();
		Boolean bool = false;
		String decrypt="" ;
		for (CompanyPubKey pubKey : userKeyList) {
			try {
				String publicKey = pubKey.getPublicKey();
				System.out.println(publicKey);
				bool= ECCUtil2.verify(resultCheck.getProductExpre()+"."+ resultCipher.getRanKey(),cipherText,publicKey);
				if(bool== true)
				{
				decrypt= resultCheck.getProductExpre();
				}
				//decrypt = ECCUtil.privateDecrypt(cipherBytes, privateKey);
				//byte[] validByte2 = ECCUtil.privateDecrypt(validByte, privateKey);
				//valid = new String(validByte2);
			} catch (Exception e) {
				log.error("【解密操作】秘钥不匹配", e);
			}
		}
		
		if (bool == true && !decrypt.isEmpty() ) {
			resultMap.put("decrypt", resultCheck.getProductExpre());
			// 次数解密
			String count = resultCipher.getCount();
			if (requestType.equals(type)) {
				resultMap.put("count", count);
			} else {
				Integer count2 = Integer.parseInt(count) + 1;
				resultMap.put("count", count2);
				// 更新次数
				resultCipher.setCount("" + count2);
				cipherMapper.updateCount(resultCipher);
			
		}}else
		{
			throw new AntiFakeException(ResultEnum.DECRYPT_ERROR);
		}
		/*} else if (valid.equals(CipherStatus.DOWN.getCode())) {
			throw new AntiFakeException(ResultEnum.CIPHER_DOWN);
		} else if (valid.equals(CipherStatus.BACK.getCode())) {
			throw new AntiFakeException(ResultEnum.CIPHER_BACK);
		}*/
		
		
		//Boolean bool= ECCUtil2.verify(template,signature,publicKey);
			//	return bool;
		return resultMap;
	}
	
	
public Map<String, Object> verifyCode(String codeString, String signature) throws Exception {
		
	/*	Map<String, Object> resultMap = new HashMap<>();
	
		String[] split = StringUtils.split(codeString, ".");
		
		String productId = split[0];
		String batch = split[1];
		//String companyId = split[2];
		Code code = new Code();
		//code.setCompanyId(Integer.parseInt(companyId));
		//code.setProductId(Integer.parseInt(productId));
		code.setCodeId(codeString);
		Code resultCode = codeMapper.queryCode(code);
		Expre resultCheck = expreMapper.queryExpreByCId(resultCode.getCompanyId(), batch);
		// 校验
		// 查询公钥
		Company company = companyMapper.selectByPrimaryKey(resultCode.getCompanyId());
		
		CompanyProduct companyProduct = new CompanyProduct();
		companyProduct.setProductId((Integer) resultCode.getProductId());
		
		
		Example<CompanyProduct> example = Example.of(companyProduct);
		List<CompanyProduct> userKeyList = companyProductRepository.findAll(example);
		
		
		
		signature = signature + resultCode.getSignature();
		Boolean bool = false;
		String decrypt="" ;
		for (CompanyProduct pubKey : userKeyList) {
			try {
				String publicKey = pubKey.getPublicKey();
				System.out.println(publicKey);
				bool= ECCUtil4.mlVerify(publicKey,signature,resultCheck.getProductExpre());
				if(bool== true)
				{
				decrypt= resultCheck.getProductExpre();
				}
				
				System.out.println(bool);
				
			} catch (Exception e) {
				log.error("【解密操作】秘钥不匹配", e);
			}
		}
		
		if (bool == true && !decrypt.isEmpty() ) {
			resultMap.put("decrypt", resultCheck.getProductExpre());
			// 次数解密
			
			
		}else
		{
			throw new AntiFakeException(ResultEnum.DECRYPT_ERROR);
		}
		return resultMap;
		*/
		return new HashMap<String,Object>();
	}

public Boolean verifyToken(String token, Integer CompanyId) throws Exception {
	
	
	Company company = companyMapper.selectByPrimaryKey(CompanyId);

	CompanyPubKey companyPubKey = new CompanyPubKey();
	companyPubKey.setCompanyId((Integer)company.getCompanyId());
	companyPubKey.setStatus(0);
	
	Example<CompanyPubKey> example = Example.of(companyPubKey);
	List<CompanyPubKey> userKeyList = companyPubKeyRepository.findAll(example);
	Boolean bool=false;
	double time =(new Date().getTime())/6000;
	int  time1 =(int) Math.rint(time);
	
	for (CompanyPubKey pubKey : userKeyList) {
		try {
			String publicKey = pubKey.getPublicKey();
			System.out.println(publicKey);
			bool= ECCUtil4.mlVerify(publicKey,token,""+time1);
			if(bool== true)
			{
			return true;
			}
			
			System.out.println(bool);
			
		} catch (Exception e) {
			log.error("【解密操作】秘钥不匹配", e);
		}
		
		
		
	}
	
	int  time2 =(int) Math.rint(time-1);
	
	for (CompanyPubKey pubKey : userKeyList) {
		try {
			String publicKey = pubKey.getPublicKey();
			System.out.println(publicKey);
			bool= ECCUtil4.mlVerify(publicKey,token,""+time2);
			if(bool== true)
			{
			return true;
			}
			
			System.out.println(bool);
			
		} catch (Exception e) {
			log.error("【解密操作】秘钥不匹配", e);
		}
		
		
		
	}
	
	
	
	return false;
}



@Override
public Map<String, Object>  getCode(String codeString) throws Exception {
	

	String[] split = StringUtils.split(codeString, ".");
	

	String batch = split[1];
	String companyId = split[2];
	Code code = new Code();
	
	code.setCodeId(codeString);
	Code resultCode = codeMapper.queryCode(code);
	
	
	
	
	Product product = productMapper.queryByProductId(resultCode.getProductId());
	
	Company company = companyMapper.selectByPrimaryKey(product.getCompanyId());
	Expre expre=expreMapper.queryExpreByCId(product.getCompanyId(), batch);
	
	
	System.out.println(company +""+resultCode+""+product);
	
	
	Map<String, Object> obj= new HashMap<>();
	obj.put("companyName", company.getCompanyName());
	obj.put("signature", resultCode.getSignature());
	obj.put("queryTimes", resultCode.getQueryTimes());
	obj.put("firstQueryTimes", resultCode.getFirstQueryTime());
	obj.put("producedDay", resultCode.getProdcedDay());
	obj.put("status", resultCode.getStatus());
	obj.put("productTitle", product.getProductTitle());
	obj.put("productDetails", product.getTemplate());
	obj.put("publicKey", product.getPublicKey());
	obj.put("message", expre.getProductExpre());
	
	return obj;
	
	
	
}

public void  updateCode() throws Exception {
	
	
	FileUploadUtil fp= new FileUploadUtil();
	
	List<String> list =fp.listFilesForFolder(new File("./src/main/resources/codes/unseen/"));
	
	if(list== null )
	{
		log.error("Files does not exists");
	}else
	{
	
	for (String fileName: list)
	{
		List<Map<String, Object>> list1=fp.read( fileName);
		for(int i=0;i<list1.size();i++)
		{
			Map<String, Object> map=list1.get(i);
			String uuid=(String)  map.get("uuid");
			String sign= (String) map.get("sign");
			
			String[] split = StringUtils.split( uuid , ".");
			
			String productId = split[0];
			String batch = split[1];
			
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			String date = simpleDateFormat.format(new Date());
			
			Code code = new Code();
			
			code.setCodeId(uuid);
			code.setSignature(sign);
			code.setProductId(Integer.parseInt(productId));
			code.setProdcedDay(date);
			code.setQueryTimes(0);
			code.setStatus(0);
			Code code1= codeMapper.queryCode(code);
			//Product product = productMapper.queryByProductId(code1.getProductId());
			
			if(code1== null)
			{
			codeMapper.insertCode(code);
			}
			else
			{
				log.error("codeId already exists"+uuid , uuid);
			}
			
		}
		}
	}
	
	
	
	
}





	
	
	
	@Override
	public List<Cipher> listCipher(Antifake antifake,String orderBy,Integer pageNum,Integer pageSize) throws Exception {
		//antifake.setPageNum(antifake.getPageNum()* antifake.getPageSize());
		pageNum = pageNum*pageSize;
		List<Cipher> listCipher = cipherMapper.listCipher(antifake,orderBy,pageNum,pageSize);
		
		//获取公钥
		// 查询公钥
		Company company = companyMapper.selectByPrimaryKey(antifake.getCompanyId());
		CompanyPubKey companyPubKey = new CompanyPubKey();
		companyPubKey.setCompanyId((Integer)company.getCompanyId());
		companyPubKey.setStatus(STATUS);
		
		Example<CompanyPubKey> example = Example.of(companyPubKey);
		List<CompanyPubKey> userKeyList = companyPubKeyRepository.findAll(example);
		/*if(listCipher.size()>0) {
			for (Cipher cipher : listCipher) {
				//byte[] validByte = Base64Utils.decodeFromString(cipher.getValid());
				for (CompanyPubKey pubKey : userKeyList) {
					try {
						String publicKey = pubKey.getPublicKey();
						ECPrivateKey privateKey = (ECPrivateKey) ECCUtil.string2PrivateKey(publicKey);
						byte[] privateDecrypt = ECCUtil.privateDecrypt(validByte, privateKey);
						String valid = new String(privateDecrypt);
						cipher.setValid(valid);

					} catch (Exception e) {
						log.error("【解密操作】秘钥不匹配", e);
					}
				}
				
			}
		}*/
		return listCipher;
	}
	
}
