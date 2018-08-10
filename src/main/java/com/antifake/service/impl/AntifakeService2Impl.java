package com.antifake.service.impl;




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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.alibaba.druid.util.HexBin;
import com.antifake.mapper.ExpreMapper;
import com.antifake.enums.CipherStatus;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CipherMapper;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.CompanyPubKeyRepository;
import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Company;
import com.antifake.model.CompanyPubKey;
import com.antifake.model.Expre;
import com.antifake.service.AntifakeService2;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.ECCUtil2;
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
	private CompanyPubKeyRepository companyPubKeyRepository;

	
	@Override
	// 待优化
	public String sign(String privateKey, Integer companyId, Integer productId,
			String template) throws Exception {
		  
		
		long start = System.currentTimeMillis();
		
		List<Cipher> listCipher = new ArrayList<Cipher>();
			
			
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		
	
		String get12uuid = UUIDUtil.get12UUID();
		
		String templates = template + "." + get12uuid;
		System.out.println(templates);
		String signature =ECCUtil2.sign(templates, privateKey);
		
		Long increment = redisTemplate.opsForValue().increment("" + companyId + "_" + productId, 1L);
		if (increment > 999999999) {
			redisTemplate.opsForValue().set("" + companyId + "_" + productId, "1");
		}
		

		Cipher cipher = new Cipher();
			System.out.println(signature);
			System.out.println("length" + signature.length());
			
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
		
		
		
		
		//String signature =util.sign(template, (ECPrivateKey) pair.getPrivate());
		System.out.println("originalbytes"+ signature);
		
		
		long end = System.currentTimeMillis();
		System.out.println("数据处理耗时："+(end-start));
		// 存储加密信息
		long start2 = System.currentTimeMillis();
		cipherMapper.insertList(listCipher);
		System.err.println("存储耗时 ：" + (System.currentTimeMillis() - start2) + "毫秒");
		
		
		return stringCode;
    	
    	
    	//Boolean bool= ECCUtil2.verify(template, signature, "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEibHtxDEbBrytInM7s6YZyHmOSeiK/GUpKB6JQRZotPZcnbtfgKQhLZsebaT7kWl1Pe6T5TCgSI32elW8HVycdw==");
    	//System.out.println(bool);
		
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
