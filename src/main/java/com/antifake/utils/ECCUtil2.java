package com.antifake.utils;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
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

//import java.security.interfaces.ECPrivateKey;
//mport java.security.interfaces.ECPublicKey;


import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

 

import com.alibaba.druid.util.HexBin;


public class ECCUtil2 {
	
	public final static byte    tag_Sequence = 0x30;
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
	    	
		// System.out.println("5bpiEyC5n8s291Kjou+PHnRTu3OQ1jiKFZKmDicjs+xJSsroOBO3fytu4zf77Wq3p3TIaIHCWfsXOVawCHPJBA==".length());
		 
		 
		 ECCUtil util= new ECCUtil();
		 ECPrivateKey ecprivateKey= util.string2PrivateKey(priStr);
    	/* byte[] keyBytes = Base64Utils.decodeFromString(priStr);
         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
         KeyFactory keyFactorys = KeyFactory.getInstance("EC" ,"BC");  
         ECPrivateKey ecprivateKey = (ECPrivateKey) keyFactorys.generatePrivate(keySpec);  */
    	
        
         
    	 PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecprivateKey.getEncoded());
    	 KeyFactory keyFactory = KeyFactory.getInstance("EC") ;
    	
    	 PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec) ;
    	 Signature signature = Signature.getInstance("SHA256withECDSA" , "BC");
    	 
    	// System.out.println("sign"+signature.toString());
    			 
    	 signature.initSign(privateKey);
    	
    	 signature.update(template.getBytes("UTF-8"));
    	 byte []arr = signature.sign();
    	
    /*	 System.out.println("Hexbin"+ HexBin.encode(arr));
    	 System.out.println("Hexbin"+ HexBin.encode(arr).length());
    	DerInputStream din=new DerInputStream(arr);
        DerValue[] value=din.getSequence(0);
        System.out.println(value[0].getBigInteger());
        DerInputStream din2=new DerInputStream(value[1].toByteArray());
        System.out.println("der"+din2.getBigInteger().toString().length());
    	*/
    	
    	
    	// return Base64.getEncoder().encodeToString(arr);
    	 return HexBin.encode(arr);
    }
	   
	 private static ASN1Primitive toAsn1Primitive(byte[] data) throws Exception
	    {
	        try (ByteArrayInputStream inStream = new ByteArrayInputStream(data);
	                ASN1InputStream asnInputStream = new ASN1InputStream(inStream);) 
	        {
	            return asnInputStream.readObject();
	        }
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
	    	 Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
	    	 signature.initVerify(publicKey);
	    	 
	    	 signature.update(template.getBytes("UTF-8"));
	    	//boolean bool = signature.verify(Base64.getDecoder().decode(arr));
	    	
	    	 boolean bool = signature.verify(HexBin.decode(arr));
	    	 System.out.println("jdk ecdsa verify:"+bool);
	    	 return bool;
	    } 
}
