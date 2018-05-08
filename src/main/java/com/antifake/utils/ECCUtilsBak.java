package com.antifake.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class ECCUtilsBak {
	
	public static final String ALGORITHM = "EC";
	public static final String PUBLIC_KEY = "ECCPublicKey";
	public static final String PRIVATE_KEY = "ECCPrivateKey";
	
	private static final Logger logger = LoggerFactory.getLogger(ECCUtilsBak.class);
	
	/**
	  * <p>Description: 生成密钥</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	public static Map<String,String> initKey() {
		
		Map<String, String> keyMap = new HashMap<String, String>(2);
		//初始化密钥   
        KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(256);  
	        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
	        ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();  
	        ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();  
	        
	        byte[] encodePrivate = Base64Utils.encode(ecPrivateKey.getEncoded());
	        byte[] encodepublic = Base64Utils.encode(ecPublicKey.getEncoded());
	        String privateKeyString = new String(encodePrivate);
	        String publicKeyString = new String(encodepublic);
	        keyMap.put(PRIVATE_KEY, privateKeyString);
	        keyMap.put(PUBLIC_KEY, publicKeyString);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}  
		return keyMap;
	}
	
	/**
	  * <p>Description: 获取公钥</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	public static String getPublicKey(Map<String,String> map) {
		return map.get(PUBLIC_KEY);
	}
	
	/**
	  * <p>Description: 获取私钥</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	public static String getPrivateKey(Map<String,String> map) {
		return map.get(PRIVATE_KEY);
	}
	
	/**
	  * <p>Description: 加密</p>
	  * @author JZR  
	 * @throws NoSuchAlgorithmException 
	  * @date 2018年4月17日
	  */
	public static String encrypt(String String,String privateKeyString) throws Exception {
		
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKeyString.getBytes()));  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);  
        Signature signature = Signature.getInstance("SHA1withECDSA");  
        signature.initSign(privateKey);  
        signature.update(String.getBytes());  
        byte[] res = signature.sign(); 
        byte[] encode = Base64Utils.encode(res);
        return new String(encode);
	}
	
	/**
	  * <p>Description: 验证</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	public static Boolean check(String string,String publicKeyString) throws Exception {
		String[] split = StringUtils.split(string, ".");
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decode(publicKeyString.getBytes())); 
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);  
        Signature signature = Signature.getInstance("SHA1withECDSA");  
        signature.initVerify(publicKey);  
        byte[] decode = Base64Utils.decode(split[1].getBytes());
        byte[] verify = Base64Utils.decode(split[0].getBytes());
        signature.update(decode);  
        boolean bool = signature.verify(verify);
        return bool;
	}
	
	/**
	  * <p>Description: 解密</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	public static String decrypt(String string) {
		String[] split = StringUtils.split(string, ".");
		byte[] decode = Base64Utils.decode(split[1].getBytes());
		return new String(decode);
	}
}
