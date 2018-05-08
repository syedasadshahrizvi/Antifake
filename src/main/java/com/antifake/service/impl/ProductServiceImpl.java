package com.antifake.service.impl;

import java.util.ArrayList;
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

import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CheckMapper;
import com.antifake.mapper.CipherMapper;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.ProductMapper;
import com.antifake.mapper.UserKeyMapper;
import com.antifake.model.Check;
import com.antifake.model.Cipher;
import com.antifake.model.Company;
import com.antifake.model.Product;
import com.antifake.model.UserKey;
import com.antifake.service.ProductService;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.MD5Utils;
import com.antifake.utils.UUIDUtil;

@Service
public class ProductServiceImpl implements ProductService{
	
	public static final String checkCount = "0";
	public static final String requestType = "S1S";
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CipherMapper cipherMapper;
	
	@Autowired
	private CheckMapper checkMapper;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private UserKeyMapper userKeyMapper;
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public Integer createProduct(Integer companyId,String companyCode,String productCode, String template) {
		//查询公司状态
		Company company = companyMapper.selectByPrimaryKey(companyId);
		if(company==null || company.getStatus()==0) {
			throw new AntiFakeException(ResultEnum.COMPANY_ERROR.getCode(), ResultEnum.COMPANY_ERROR.getMessage());
		}
		Integer flag = productMapper.createProduct(companyId,companyCode,productCode,template);
		return flag;
	}

	@Override
	public List<String> encrypt(String privateKey, String companyCode, String productCode, String template,
			Integer count) {
		
		List<String> listString = new ArrayList<String>();
		List<Cipher> listCipher = new ArrayList<Cipher>();
		List<Check> listCheck = new ArrayList<Check>();
		
		for(Integer i=0;i<count;i++) {
			//递增
			Long increment = redisTemplate.opsForValue().increment(""+companyCode+"_"+productCode, 1L);
			if(increment>999999999) {
				redisTemplate.opsForValue().set(""+companyCode+"_"+productCode, "1");
			}
			String get12uuid = UUIDUtil.get12UUID();
			//加密
			try {
				Cipher cipher = new Cipher();
				Check check = new Check();
				String templates = template+get12uuid;
				//获取私钥
				ECPublicKey publicKey = ECCUtil.string2PublicKey(privateKey);
				//加密
				byte[] publicEncrypt = ECCUtil.publicEncrypt(templates.getBytes(), publicKey);
				String encrypt = Base64Utils.encodeToString(publicEncrypt);
				String substringBegin = encrypt.substring(0, 10);
				String substringLast = StringUtils.substring(encrypt, 10);
				System.out.println(templates);
				String newString = substringLast+"."+MD5Utils.hash(templates);
				String stringCode = ""+companyCode+"."+productCode+"."+substringBegin+"."+increment;
				cipher.setCompanyCode(companyCode);
				cipher.setProductCode(productCode);
				cipher.setCipherText(newString);
				cipher.setCode(""+increment);
				check.setCompanyCode(companyCode);
				check.setProductCode(productCode);
				check.setCode(""+increment);
				//解密次数(后期改成密文)
				check.setCount("0");
				listCipher.add(cipher);
				listString.add(stringCode);
				listCheck.add(check);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//存储加密信息
		cipherMapper.insertList(listCipher);
		checkMapper.insertList(listCheck);
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
		Check check = new Check();
		check.setCompanyCode(companyCode);
		check.setProductCode(productCode);
		check.setCode(increment);
		Check resultCheck = checkMapper.queryCheck(check);
		//校验
		//查询公钥
		Product product = productMapper.queryByCCode(companyCode);
		Company company = companyMapper.selectByPrimaryKey(product.getCompanyId());
		List<UserKey> userKeyList = userKeyMapper.selectKeyByUid(company.getUserId());
		String publicKey = userKeyList.get(0).getPublicKey();
		//String[] splitCheck = StringUtils.split(resultCheck.getCount(), ".");
		//完整密文
		String[] cipherTextSplit = StringUtils.split(resultCipher.getCipherText(),".");
		cipherText=cipherText+cipherTextSplit[0];
		String cipherMd5 = cipherTextSplit[1];
		//解密
		byte[] cipherBytes = Base64Utils.decodeFromString(cipherText);
		ECPrivateKey privateKey = ECCUtil.string2PrivateKey(publicKey);
		byte[] decrypt = ECCUtil.privateDecrypt(cipherBytes, privateKey);
		String decryptString = new String(decrypt);
		if(MD5Utils.hash(decryptString).equals(cipherMd5)) {
			resultMap.put("decrypt", decryptString);
			//次数解密
			String count = resultCheck.getCount();
			if(requestType.equals(type)) {
				resultMap.put("count", count);
			}else {
				Integer count2 = Integer.parseInt(count)+1;
				resultMap.put("count", count2);
				//更新次数
				resultCheck.setCount(""+count2);
				checkMapper.updateCount(resultCheck);
			}
		}else {
			//假冒
			resultMap.put("decrypt", "");
		}
		return resultMap;
	}

	@Override
	public Product getProductBypId(Integer productId) {
		return productMapper.queryByProductId(productId);
	}

	@Override
	public List<Product> getProductList(Integer companyId) {
		return productMapper.queryByCId(companyId);
	}
}
