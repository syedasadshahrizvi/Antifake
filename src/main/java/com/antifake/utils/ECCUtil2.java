package com.antifake.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.util.Base64Utils;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

 

import com.alibaba.druid.util.HexBin;

public class ECCUtil2 {
	 public static KeyPair generateKeyPair() throws Exception {  
	    	KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
	        keyPairGenerator.initialize(256);
	    	KeyPair keypair = keyPairGenerator.generateKeyPair() ;
	    	return keypair;
	    	
	    }  
	    
	 public static ECPrivateKey string2PrivateKey(String priStr) throws Exception{  
	        byte[] keyBytes = Base64Utils.decodeFromString(priStr);
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");  
	        ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);  
	        return privateKey;  
	    } 
	 
	 public static String sign(String template, String priStr) throws Exception {
	    	
	    	
    	 byte[] keyBytes = Base64Utils.decodeFromString(priStr);
         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
         KeyFactory keyFactorys = KeyFactory.getInstance("EC");  
         ECPrivateKey ecprivateKey = (ECPrivateKey) keyFactorys.generatePrivate(keySpec);  
    	
         System.out.println(ecprivateKey);
         
    	 PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecprivateKey.getEncoded());
    	 KeyFactory keyFactory = KeyFactory.getInstance("EC") ;
    	 PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec) ;
    	 Signature signature = Signature.getInstance("SHA256withECDSA");
    	 signature.initSign(privateKey);
    	 signature.update(template.getBytes("UTF-8"));
    	 byte []arr = signature.sign();
    	 System.out.println(arr);
    	
    	 return Base64.getEncoder().encodeToString(arr);
       // return Base64.getEncoder().encodeToString(signature);
    }
	    
	  public static boolean verify(String template ,  String arr, String pubStr)  throws Exception {
		  
		//  System.out.println("jdk ecdsa sign hex decoding  :"+ HexBin.decode(arr));
		   byte[] keyBytes = Base64Utils.decodeFromString(pubStr);  
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
	        KeyFactory keyFactorys = KeyFactory.getInstance("EC");  
	        ECPublicKey ecPublicKey = (ECPublicKey) keyFactorys.generatePublic(keySpec); 
		  
	    	 X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
	    	 KeyFactory keyFactory = KeyFactory.getInstance("EC");
	    	 PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
	    	 Signature signature = Signature.getInstance("SHA256withECDSA");
	    	 signature.initVerify(publicKey);
	    	 
	    	 signature.update(template.getBytes("UTF-8"));
	    	 boolean bool = signature.verify(Base64.getDecoder().decode(arr));
	    	
	    	// boolean bool = signature.verify(HexBin.decode(arr));
	    	 System.out.println("jdk ecdsa verify:"+bool);
	    	 return bool;
	    } 
}
