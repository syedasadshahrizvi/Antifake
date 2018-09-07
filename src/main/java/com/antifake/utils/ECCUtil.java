package com.antifake.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.ECParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

//import org.bouncycastle.asn1.sec.ECPrivateKey;
//import java.security.*;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;

//import java.security.interfaces.ECPrivateKey;
 //import java.security.interfaces.ECPublicKey;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.util.Base64Utils;

import com.alibaba.druid.util.HexBin;  
  
  
public class ECCUtil {  
    static {  
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
    }  
      
    //生成秘钥对  
    public static KeyPair getKeyPair() throws Exception {  
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");  
        keyPairGenerator.initialize(256, new SecureRandom());  
        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
        return keyPair;  
    }  
      
    //获取公钥(Base64编码)  
    public static String getPublicKey(KeyPair keyPair){  
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic(); 
        byte[] bytes = publicKey.getEncoded(); 
        return Base64Utils.encodeToString(bytes);  
    }  
      
    //获取私钥(Base64编码)  
    public static String getPrivateKey(KeyPair keyPair) throws IOException{  
    	//System.out.println("private key"+keyPair.getPrivate());
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();  
        //System.out.println("private key EC"+privateKey);
        byte[] bytes = privateKey.getEncoded();  
        //System.out.println("private key EC bytes"+bytes);
        //System.out.println("private key EC base64"+Base64Utils.encodeToString(bytes));
        return Base64Utils.encodeToString(bytes);  
    }  
      
    //将Base64编码后的公钥转换成PublicKey对象  
    public static ECPublicKey string2PublicKey(String pubStr) throws Exception{  
        byte[] keyBytes = Base64Utils.decodeFromString(pubStr);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");  
        ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(keySpec);  
        return publicKey;  
    }  
      
    //将Base64编码后的私钥转换成PrivateKey对象  
    public static ECPrivateKey string2PrivateKey(String priStr) throws Exception{  
        byte[] keyBytes = Base64Utils.decodeFromString(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");  
        ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);  
        return privateKey;  
    }  
    
      
    //公钥加密  
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{  
        Cipher cipher = Cipher.getInstance("ECIES", "BC");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        byte[] bytes = cipher.doFinal(content);  
        
        return bytes;  
    }  
      
    //私钥解密  
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{  
        Cipher cipher = Cipher.getInstance("ECIES", "BC");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        byte[] bytes = cipher.doFinal(content);  
        return bytes;  
    }  
    
    public static HashMap<String, Object> getPublickey() throws Exception
    {
    	
    	 KeyPair keyPair = ECCUtil.getKeyPair();  
    	
         String publicKeyStr = ECCUtil.getPublicKey(keyPair);   
         System.out.println(publicKeyStr);
         String privateKeyStr = ECCUtil.getPrivateKey(keyPair);
         System.out.println(privateKeyStr);
         
         X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
         // 设置序列号  
         certGen.setSerialNumber(new BigInteger("123"));  
         // 设置颁发者  
         certGen.setIssuerDN(new X500Principal("C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=SICCA"));  
         // 设置有效期  
         Calendar c = Calendar.getInstance();
         c.add(Calendar.DATE, - 7);
         certGen.setNotBefore(c.getTime());  
         c.add(Calendar.YEAR, 7);
         certGen.setNotAfter(c.getTime());  
         // 设置使用者  
         certGen.setSubjectDN(new X500Principal("C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=" + "JZR"));  
         // 公钥  
         certGen.setPublicKey(keyPair.getPublic());  
         // 签名算法  
         certGen.setSignatureAlgorithm("SHA1withECDSA");  
         X509Certificate cert = certGen.generateX509Certificate(keyPair.getPrivate(), "BC");   
         
         
         HashMap<String, Object> map=new HashMap<>();
         map.put("1",publicKeyStr);
         map.put("2", cert.getEncoded().toString());
      
         return map;
    }
      
    

    public static KeyPair generateKeyPair() throws Exception {  
    	KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(256);
    	KeyPair keypair = keyPairGenerator.generateKeyPair() ;
    	
    	
    	return keypair;
    	
    }  
    
    public static String sign(String template, String priStr) throws Exception {
    	
    	
    	 byte[] keyBytes = Base64Utils.decodeFromString(priStr);
         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
         KeyFactory keyFactorys = KeyFactory.getInstance("EC", "BC");  
         ECPrivateKey ecprivateKey = (ECPrivateKey) keyFactorys.generatePrivate(keySpec);  
    	
         System.out.println(ecprivateKey);
         
    	 PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecprivateKey.getEncoded());
    	 KeyFactory keyFactory = KeyFactory.getInstance("EC") ;
    	 PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec) ;
    	 Signature signature = Signature.getInstance("SHA256withECDSA");
    	 signature.initSign(privateKey);
    	 signature.update(template.getBytes());
    	 byte []arr = signature.sign();
    	 System.out.println(arr);
    	 System.out.println("jdk ecdsa sign :"+ HexBin.encode(arr));
    	 return HexBin.encode(arr);
       // return Base64.getEncoder().encodeToString(signature);
    }
    
    public static boolean verify(String template ,  String arr, ECPublicKey ecPublicKey)  throws Exception {
    	 X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
    	 KeyFactory keyFactory = KeyFactory.getInstance("EC");
    	 PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
    	 Signature signature = Signature.getInstance("SHA256withECDSA");
    	 signature.initVerify(publicKey);
    	 signature.update(template.getBytes());
    	 boolean bool = signature.verify(HexBin.decode(arr));
    	 System.out.println("jdk ecdsa verify:"+bool);
    	 return bool;
    } 
    
    
    public static void main(String[] args) throws Exception {  
    	
    	KeyPair pair = getKeyPair();
    	System.out.println( pair.getPrivate()); 
    	System.out.println( pair.getPublic()); 
    	
    	/*ECPrivateKey privateKey = (ECPrivateKey) pair.getPrivate();  
        byte[] bytes1 = privateKey.getEncoded();  
        System.out.println( "private "+ Base64Utils.encodeToString(bytes1)); 
        
    	//String signature = sign("foobar", (ECPrivateKey)pair.getPrivate());
        String signature = sign("foobar", Base64Utils.encodeToString(bytes1));
    	System.out.println(signature);

    //	Let's check the signature
    	boolean isCorrect = verify("foobar", signature, (ECPublicKey)pair.getPublic());
    	System.out.println("Signature correct: " + isCorrect);
    	
    	
        KeyPair keyPair = ECCUtil.getKeyPair();  
        String publicKeyStr = ECCUtil.getPublicKey(keyPair);  
        String privateKeyStr = ECCUtil.getPrivateKey(keyPair);  
        System.out.println("ECC公钥Base64编码:" + publicKeyStr);  
        System.out.println("ECC私钥Base64编码:" + privateKeyStr); 
        
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        // 设置序列号  
        certGen.setSerialNumber(new BigInteger("123"));  
        // 设置颁发者  
        certGen.setIssuerDN(new X500Principal("C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=SICCA"));  
        // 设置有效期  
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - 7);
        certGen.setNotBefore(c.getTime());  
        c.add(Calendar.YEAR, 7);
        certGen.setNotAfter(c.getTime());  
        // 设置使用者  
        certGen.setSubjectDN(new X500Principal("C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=" + "TestCer"));  
        // 公钥  
        certGen.setPublicKey(keyPair.getPublic()); 
        // 签名算法  
        certGen.setSignatureAlgorithm("SHA1withECDSA");  
        X509Certificate cert = certGen.generateX509Certificate(keyPair.getPrivate(), "BC"); 
        String certPath = "d:/TestCer.cer";  
        
        FileOutputStream fos = new FileOutputStream(certPath);  
        fos.write(cert.getEncoded());  
        fos.close(); 
        
        CertificateFactory certificateFactory = new CertificateFactory();
        X509Certificate instance = (X509Certificate)certificateFactory.engineGenerateCertificate(new FileInputStream("d:/TestCer.cer"));
        PublicKey publicKey2 = instance.getPublicKey();
        byte[] encoded2 = publicKey2.getEncoded();
        String encodeToString3 = Base64Utils.encodeToString(encoded2);
        System.out.println("pk:"+encodeToString3);
        
        
        ECPublicKey pk = (ECPublicKey)cert.getPublicKey(); 
        byte[] encoded = pk.getEncoded();
        String encodeToString = Base64Utils.encodeToString(encoded);
        System.out.println("pk:"+encodeToString);  
        
        //ECPublicKey publicKey = string2PublicKey(publicKeyStr);  
        ECPrivateKey privateKey = string2PrivateKey(privateKeyStr);  
          
        byte[] publicEncrypt = publicEncrypt("Hello World!".getBytes(), publicKey2);  
        String encodeToString2 = Base64Utils.encodeToString(publicEncrypt);
        //System.out.println(encodeToString2);
        byte[] decodeFromString = Base64Utils.decodeFromString(encodeToString2);
        byte[] privateDecrypt = privateDecrypt(decodeFromString, privateKey);  
        System.out.println(new String(privateDecrypt));  */
    }  
}  