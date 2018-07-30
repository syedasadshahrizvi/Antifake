package com.antifake.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.antifake.VO.ExpreVO;
import com.antifake.enums.CipherStatus;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CipherMapper;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.CompanyPubKeyRepository;
import com.antifake.mapper.ExpreMapper;
import com.antifake.mapper.PubKeyRepository;
import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Company;
import com.antifake.model.CompanyPubKey;
import com.antifake.model.Expre;
import com.antifake.model.PubKey;
import com.antifake.service.AntifakeService;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.MD5Utils;
import com.antifake.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class AntifakeServiceImpl implements AntifakeService {

	public static final String checkCount = "0";
	public static final String requestType = "S1S";
	public static final Integer STATUS = 0;

	@Autowired
	private CipherMapper cipherMapper;

	@Autowired
	private ExpreMapper expreMapper;

	@Autowired
	private PubKeyRepository pubKeyRepository;
	
	@Autowired
	private CompanyPubKeyRepository companyPubKeyRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private CompanyMapper companyMapper;

	@Override
	// 待优化
	public List<String> encrypt(String privateKey, Integer companyId, String companyCode, String productCode,
			String template, Integer num) {
		 // 开始时间
        long start = System.currentTimeMillis();
		List<String> listString = new ArrayList<String>();
		List<Cipher> listCipher = new ArrayList<Cipher>();
		// 存储明文
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		String expreMD5 = MD5Utils.hash(template);

		for (Integer i = 0; i < num; i++) {
			// 递增
			Long increment = redisTemplate.opsForValue().increment("" + companyCode + "_" + productCode, 1L);
			if (increment > 999999999) {
				redisTemplate.opsForValue().set("" + companyCode + "_" + productCode, "1");
			}
			String get12uuid = UUIDUtil.get12UUID();
			// 加密
			try {
				Cipher cipher = new Cipher();
				String templates = expreMD5 + "." + get12uuid;
				// 获取私钥
				ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
				// 加密
				byte[] publicEncrypt = ECCUtil.publicEncrypt(templates.getBytes(), publicKey);
				// 启用加密
				//byte[] validByte = ECCUtil.publicEncrypt(CipherStatus.UP.getCode().getBytes(), publicKey);
				String encrypt = Base64Utils.encodeToString(publicEncrypt);
				//String valid = Base64Utils.encodeToString(validByte);
				String substringBegin = encrypt.substring(0, 10);
				String substringLast = StringUtils.substring(encrypt, 10);
				String stringCode = "" + companyCode + "." + productCode + "." + substringBegin + "." + increment;
				cipher.setCompanyId(companyId);
				cipher.setCompanyCode(companyCode);
				cipher.setProductCode(productCode);
				cipher.setCipherText(substringLast);
				cipher.setBatch(batch);
				cipher.setCode("" + increment);
				//cipher.setValid(valid);
				cipher.setValid(Base64Utils.encodeToString(ECCUtil.publicEncrypt(CipherStatus.UP.getCode().getBytes(), publicKey)));
				listCipher.add(cipher);
				listString.add(stringCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 long end = System.currentTimeMillis();
		 System.out.println("数据处理耗时："+(end-start));
		// 存储加密信息
		long start2 = System.currentTimeMillis();
		cipherMapper.insertList(listCipher);
		System.err.println("存储耗时 ：" + (System.currentTimeMillis() - start2) + "毫秒");
		return listString;
	}
	
	@Override
	// 待优化
	public List<String> encrypt2(String privateKey, Integer companyId, String companyCode, String productCode,
			String template, Integer num) {
		
		 // 开始时间
        long start = System.currentTimeMillis();
		List<String> listString = new ArrayList<String>();
		List<Cipher> listCipher = new ArrayList<Cipher>();
		// 存储明文
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		String expreMD5 = MD5Utils.hash(template);

		for (Integer i = 0; i < num; i++) {
			// 递增
			Long increment = redisTemplate.opsForValue().increment("" + companyCode + "_" + productCode, 1L);
			if (increment > 999999999) {
				redisTemplate.opsForValue().set("" + companyCode + "_" + productCode, "1");
			}
			String get12uuid = UUIDUtil.get12UUID();
			// 加密
			try {
				Cipher cipher = new Cipher();
				String templates = expreMD5 + "." + get12uuid;
				// 获取私钥
				ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
				// 加密
				byte[] publicEncrypt = ECCUtil.publicEncrypt(templates.getBytes(), publicKey);
				// 启用加密
				byte[] validByte = ECCUtil.publicEncrypt(CipherStatus.UP.getCode().getBytes(), publicKey);
				String encrypt = Base64Utils.encodeToString(publicEncrypt);
				String valid = Base64Utils.encodeToString(validByte);
				String substringBegin = encrypt.substring(0, 10);
				String substringLast = StringUtils.substring(encrypt, 10);
				String stringCode = "" + companyCode + "." + productCode + "." + substringBegin + "." + increment;
				cipher.setCompanyId(companyId);
				cipher.setCompanyCode(companyCode);
				cipher.setProductCode(productCode);
				cipher.setCipherText(substringLast);
				cipher.setBatch(batch);
				cipher.setCode("" + increment);
				cipher.setValid(valid);
				cipher.setValid(Base64Utils.encodeToString(ECCUtil.publicEncrypt("0".getBytes(), publicKey)));
				listCipher.add(cipher);
				listString.add(stringCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 存储加密信息
		//cipherMapper.insertList(listCipher);
		System.err.println("数据处理耗时 ：" + (System.currentTimeMillis() - start) + "毫秒");
		return listString;
	}

	@Override
	public Map<String, Object> checkCode(String codeString, String type) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String[] split = StringUtils.split(codeString, ".");
		String companyCode = split[0];
		String productCode = split[1];
		String cipherText = split[2];
		String increment = split[3];
		Cipher cipher = new Cipher();
		cipher.setCompanyCode(companyCode);
		cipher.setProductCode(productCode);
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
		
		// 完整密文

		cipherText = cipherText + resultCipher.getCipherText();
		// 解密
		byte[] cipherBytes = Base64Utils.decodeFromString(cipherText);
		byte[] validByte = Base64Utils.decodeFromString(resultCipher.getValid());
		byte[] decrypt = null;
		String valid = null;
		for (CompanyPubKey pubKey : userKeyList) {
			try {
				String publicKey = pubKey.getPublicKey();
				ECPrivateKey privateKey = ECCUtil.string2PrivateKey(publicKey);
				decrypt = ECCUtil.privateDecrypt(cipherBytes, privateKey);
				byte[] validByte2 = ECCUtil.privateDecrypt(validByte, privateKey);
				valid = new String(validByte2);
			} catch (Exception e) {
				log.error("【解密操作】秘钥不匹配", e);
			}
		}
		if (decrypt == null || valid == null) {
			throw new AntiFakeException(ResultEnum.DECRYPT_ERROR);
		} else if (valid.equals(CipherStatus.DOWN.getCode())) {
			throw new AntiFakeException(ResultEnum.CIPHER_DOWN);
		} else if (valid.equals(CipherStatus.BACK.getCode())) {
			throw new AntiFakeException(ResultEnum.CIPHER_BACK);
		}
		String decryptString = new String(decrypt);
		String cipherMd5 = StringUtils.split(decryptString, ".")[0];
		if (MD5Utils.hash(resultCheck.getProductExpre()).equals(cipherMd5)) {
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
			}
		} else {
			// 假冒
			throw new AntiFakeException(ResultEnum.DECRYPT_ERROR);
		}
		return resultMap;
	}

/*	@Override
	public Cipher updateCipher(Integer cipherId, Integer companyId, String privateKey, String status) throws Exception {
		// 获取私钥
		ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
		// 启用加密
		byte[] validByte = null;
		if (status.equals(CipherStatus.DOWN.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.DOWN.getCode().getBytes(), publicKey);
		} else if (status.equals(CipherStatus.BACK.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.BACK.getCode().getBytes(), publicKey);
		} else {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		String valid = Base64Utils.encodeToString(validByte);
		Cipher cipher = new Cipher();
		cipher.setCipherId(cipherId);
		cipher.setCompanyId(companyId);
		cipher.setValid(valid);
		cipherMapper.updateValid(cipher);
		return cipher;
	}*/
	
	@Override
	public Cipher updateCipher(Antifake antifake) throws Exception {
		// 获取私钥
		ECPublicKey publicKey = ECCUtil.string2PublicKey(antifake.getPrivateKey());
		// 启用加密
		byte[] validByte = null;
		if (antifake.getStatus().equals(CipherStatus.DOWN.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.DOWN.getCode().getBytes(), publicKey);
		} else if (antifake.getStatus().equals(CipherStatus.BACK.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.BACK.getCode().getBytes(), publicKey);
		} else {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		String valid = Base64Utils.encodeToString(validByte);
		Cipher cipher = new Cipher();
		cipher.setCipherId(antifake.getCipherId());
		cipher.setCompanyId(antifake.getCompanyId());
		cipher.setValid(valid);
		cipherMapper.updateValid(cipher);
		return cipher;
	}

	@Override
	public Integer updateButchCipher(Integer companyId, String batch, String privateKey, String status)
			throws Exception {
		// 获取密钥
		ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
		// 启用加密
		byte[] validByte = null;
		if (status.equals(CipherStatus.DOWN.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.DOWN.getCode().getBytes(), publicKey);
		} else if (status.equals(CipherStatus.BACK.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.BACK.getCode().getBytes(), publicKey);
		} else {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		String valid = Base64Utils.encodeToString(validByte);
		Cipher cipher = new Cipher();
		cipher.setCompanyId(companyId);
		cipher.setBatch(batch);
		cipher.setValid(valid);
		Integer flag = cipherMapper.updateButchValid(cipher);
		return flag;
	}

	/*@Override
	public Integer updateCodeCipher(Integer companyId, String begain, String end, String privateKey, String status)
			throws Exception {
		// 获取密钥
		ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
		// 启用加密
		byte[] validByte = null;
		if (status.equals(CipherStatus.DOWN.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.DOWN.getCode().getBytes(), publicKey);
		} else if (status.equals(CipherStatus.BACK.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.BACK.getCode().getBytes(), publicKey);
		} else {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		String valid = Base64Utils.encodeToString(validByte);
		Integer flag = cipherMapper.updateValidByCode(companyId, begain, end, valid);
		return flag;
	}*/

	@Override
	public Integer updateCodeCipher(Antifake antifake)
			throws Exception {
		// 获取密钥
		ECPublicKey publicKey = ECCUtil.string2PublicKey(antifake.getPrivateKey());
		// 启用加密
		byte[] validByte = null;
		if (antifake.getStatus().equals(CipherStatus.DOWN.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.DOWN.getCode().getBytes(), publicKey);
		} else if (antifake.getStatus().equals(CipherStatus.BACK.getCode())) {
			validByte = ECCUtil.publicEncrypt(CipherStatus.BACK.getCode().getBytes(), publicKey);
		} else {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		String valid = Base64Utils.encodeToString(validByte);
		Integer flag = cipherMapper.updateValidByCode(antifake.getCompanyId(), antifake.getBegain(),antifake.getEnd() , valid);
		return flag;
	}
	
	/*@Override
	public List<Cipher> listCipher(Integer companyId, String companyCode, String productCode, String batch,
			String orderBy, Integer pageNum, Integer pageSize) throws Exception {
		pageNum = pageNum*pageSize;
		List<Cipher> listCipher = cipherMapper.listCipher(companyId,companyCode,productCode,batch,orderBy,pageNum,pageSize);
		
		//获取公钥
		// 查询公钥
		Company company = companyMapper.selectByPrimaryKey(companyId);
		PubKey pubKeyExample = new PubKey();
		pubKeyExample.setUserId(company.getUserId());
		pubKeyExample.setStatus(STATUS);
		Example<PubKey> example = Example.of(pubKeyExample);
		List<PubKey> userKeyList = pubKeyRepository.findAll(example);
		if(listCipher.size()>0) {
			for (Cipher cipher : listCipher) {
				byte[] validByte = Base64Utils.decodeFromString(cipher.getValid());
				for (PubKey pubKey : userKeyList) {
					try {
						String publicKey = pubKey.getPublicKey();
						ECPrivateKey privateKey = ECCUtil.string2PrivateKey(publicKey);
						byte[] privateDecrypt = ECCUtil.privateDecrypt(validByte, privateKey);
						String valid = new String(privateDecrypt);
						cipher.setValid(valid);

					} catch (Exception e) {
						log.error("【解密操作】秘钥不匹配", e);
					}
				}
	 			
			}
		}
		return listCipher;
	}*/
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
		if(listCipher.size()>0) {
			for (Cipher cipher : listCipher) {
				byte[] validByte = Base64Utils.decodeFromString(cipher.getValid());
				for (CompanyPubKey pubKey : userKeyList) {
					try {
						String publicKey = pubKey.getPublicKey();
						ECPrivateKey privateKey = ECCUtil.string2PrivateKey(publicKey);
						byte[] privateDecrypt = ECCUtil.privateDecrypt(validByte, privateKey);
						String valid = new String(privateDecrypt);
						cipher.setValid(valid);

					} catch (Exception e) {
						log.error("【解密操作】秘钥不匹配", e);
					}
				}
				
			}
		}
		return listCipher;
	}
	
	
	@Override
	public List<ExpreVO> listExpre(Integer companyId,Integer pageNum,Integer pageSize) {
		pageNum = pageNum*pageSize;
		List<Expre> expreList = expreMapper.queryExpreByCoId(companyId,pageNum,pageSize);
		List<ExpreVO> expreVOList = new ArrayList<>();
		for (Expre expre : expreList) {
			Integer total = cipherMapper.getCount(expre.getBatch());
			Integer use = cipherMapper.getUseCount(expre.getBatch());
			ExpreVO expreVO = new ExpreVO();
			BeanUtils.copyProperties(expre, expreVO);
			expreVO.setTotal(total);
			expreVO.setUse(use);
			expreVOList.add(expreVO);
		}
		return expreVOList;
	}

}
