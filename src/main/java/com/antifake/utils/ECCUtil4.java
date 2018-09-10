package com.antifake.utils;

//import android.util.Base64;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.math.BigInteger;
import java.util.Date;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Base64;
import org.joda.time.DateTime;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.Digest;

public class ECCUtil4 {
	
	
	public static boolean mlVerify(String publicKey, String signature, String message) {
		 
		byte[] rs = Base64.decode(signature);
		
        byte[] pub = Base64.decode(publicKey);
		byte[]m= message.getBytes();
	    Digest digest = new SHA256Digest();
	    digest.update(m, 0, m.length);
	    byte[]hash = new byte[digest.getDigestSize()];
	    digest.doFinal(hash, 0);

	    BigInteger R = BigIntegers.fromUnsignedByteArray(rs, 0, 32).abs();
	    BigInteger S = BigIntegers.fromUnsignedByteArray(rs, 32, 32).abs();

	    X9ECParameters params = SECNamedCurves.getByName("secp256k1");
	    ECCurve ecCurve= params.getCurve();
	    ECDomainParameters ecParams = new ECDomainParameters(ecCurve,params.getG(), params.getN(), params.getH());
	    ECPublicKeyParameters pubKeyParams = new ECPublicKeyParameters(ecCurve.decodePoint(pub), ecParams);
	    ECDSASigner signer = new ECDSASigner();
	    signer.init(false, pubKeyParams);
	    return signer.verifySignature(hash, R, S);
	}
   
	/*private void showProdDetails(String ProdDetails,String companyName,String producedDate) {
	    TextView prod_details = findViewById(R.id.prod_details);
	    try {
	        JSONObject mlProdBody = new JSONObject(ProdDetails);
	        Iterator<?> it = mlProdBody.keys();
	        while(it.hasNext()) {
	           String key = it.next().toString();
	           String val = mlProdBody.getString(key);
	            prod_details.append(key + mlProdBody.getString(key));
	           //如果是保质期项目，去比较生产日期
	            if ((key=="保质期")&&(val != "-1"))
	            compDate(producedDate, val);
	            //如果是生产商项目，去比较名称是否一致
	            if ((key=="生产商")&&(val != companyName))
	                showVerifyResult("ERROR！", "假冒产品！该产品实际生产商是：" + companyName);

	        }
	    }
	            catch (JSONException e){
	            e.printStackTrace();
	        }
	 }*/
	public static void main(String args[]) throws Exception
    {
		
		
		//String signature1 = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEoEgYCa6iQChjze8JGamCPM7yQXymcerE7Y3yAnrrZ1RY4NF0uIeSOHofza+yxqoeioq1OXp7jkY+bCpvjrwhxQ==";
		/*int signature = "+W40U3THE0egW3q1YwqQBc7S4qhIT3XF8Zge4iy8SwYiApREG9WE5jik4LvJI74MA8GJrdca+oWHlMJBzzmrvQ==".length();
		System.out.println(signature);
		//System.out.println(signature1);
        String publicKey = "BHiS8u5M6/sA9EmIsnQABcv3LG0A1AC2pC0f9Z6gavwP/LdrDrT7sl91yWKAychTRSaBHWQHrME2wA1Da50DIBo=";
		String message = "hello world!";*/
		
		long date= new Date().getTime();
		float a= 16.51f;
		float b=16.41f;
		
		int a1= (int) a;
		Math.rint(date);
		long b1= (long) b;
		double time =(new Date().getTime())/6000;
		int  time1 =(int) Math.rint(time);
	
		System.out.println(time1);
		System.out.println(Math.rint(b));
//		System.out.println(b1);
//		System.out.println(date);
		
	
    }
    
    
}


