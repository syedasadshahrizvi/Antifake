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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.antifake.mapper.ExpreMapper;
import com.antifake.mapper.CipherMapper;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.ProductMapper;
import com.antifake.mapper.PubKeyMapper;
import com.antifake.model.Expre;
import com.antifake.model.Cipher;
import com.antifake.model.Company;
import com.antifake.model.Product;
import com.antifake.model.PubKey;
import com.antifake.service.AntifakeService;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.MD5Utils;
import com.antifake.utils.UUIDUtil;

@Service
public class AntifakeServiceImpl implements AntifakeService{
	
	public static final String checkCount = "0";
	public static final String requestType = "S1S";
	
	@Autowired
	private CipherMapper cipherMapper;
	
	@Autowired
	private ExpreMapper expreMapper;
	
	@Autowired
	private PubKeyMapper userKeyMapper;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Override
	public List<String> encrypt(String privateKey,Integer companyId, String companyCode, String productCode, String template,
			Integer num) {
		
		List<String> listString = new ArrayList<String>();
		List<Cipher> listCipher = new ArrayList<Cipher>();
		//存储明文
		Expre expre = new Expre();
		expre.setCompanyId(companyId);
		expre.setProductExpre(template);
		String batch = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		expre.setBatch(batch);
		expreMapper.insertExpre(expre);
		String expreMD5 = MD5Utils.hash(template);
		
		for(Integer i=0;i<num;i++) {
			//递增
			Long increment = redisTemplate.opsForValue().increment(""+companyCode+"_"+productCode, 1L);
			if(increment>999999999) {
				redisTemplate.opsForValue().set(""+companyCode+"_"+productCode, "1");
			}
			String get12uuid = UUIDUtil.get12UUID();
			//加密
			try {
				Cipher cipher = new Cipher();
				String templates = expreMD5+"."+get12uuid;
				//获取私钥
				ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
				//加密
				byte[] publicEncrypt = ECCUtil.publicEncrypt(templates.getBytes(), publicKey);
				String encrypt = Base64Utils.encodeToString(publicEncrypt);
				String substringBegin = encrypt.substring(0, 10);
				String substringLast = StringUtils.substring(encrypt, 10);
				String stringCode = ""+companyCode+"."+productCode+"."+substringBegin+"."+increment;
				cipher.setCompanyId(companyId);
				cipher.setCompanyCode(companyCode);
				cipher.setProductCode(productCode);
				cipher.setCipherText(substringLast);
				cipher.setBatch(batch);
				cipher.setCode(""+increment);
				cipher.setValid(Base64Utils.encodeToString(ECCUtil.publicEncrypt("0".getBytes(), publicKey)));
				listCipher.add(cipher);
				listString.add(stringCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//存储加密信息
		cipherMapper.insertList(listCipher);
		return listString;
	}

	@Override
	public Map<String,Object> checkCode(String codeString,String type) throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
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
		Expre resultCheck = expreMapper.queryExpreByCId(resultCipher.getCompanyId(),resultCipher.getBatch());
		//校验
		//查询公钥
		Company company = companyMapper.selectByPrimaryKey(cipher.getCompanyId());
		List<PubKey> userKeyList = userKeyMapper.selectKeyByUid(company.getUserId());
		String publicKey = userKeyList.get(0).getPublicKey();
		//String[] splitCheck = StringUtils.split(resultCheck.getCount(), ".");
		//完整密文
		
		cipherText=cipherText+resultCipher.getCipherText();
		//解密
		byte[] cipherBytes = Base64Utils.decodeFromString(cipherText);
		ECPrivateKey privateKey = ECCUtil.string2PrivateKey(publicKey);
		byte[] decrypt = ECCUtil.privateDecrypt(cipherBytes, privateKey);
		String decryptString = new String(decrypt);
		String cipherMd5 = StringUtils.split(decryptString, ".")[0];
		if(MD5Utils.hash(resultCheck.getProductExpre()).equals(cipherMd5)) {
			resultMap.put("decrypt", decryptString);
			//次数解密
			String count = resultCipher.getCount();
			if(requestType.equals(type)) {
				resultMap.put("count", count);
			}else {
				Integer count2 = Integer.parseInt(count)+1;
				resultMap.put("count", count2);
				//更新次数
				resultCipher.setCount(""+count2);
				cipherMapper.updateCount(resultCipher);
			}
		}else {
			//假冒
			resultMap.put("decrypt", "");
		}
		return resultMap;
	}
}
