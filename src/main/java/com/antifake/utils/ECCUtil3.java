package com.antifake.utils;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.springframework.util.Base64Utils;

import com.alibaba.druid.util.HexBin;

import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.math.ec.ECCurve;


public class ECCUtil3 {
	
	
	    static final X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
	    static final ECDomainParameters domain = new ECDomainParameters(curve.getCurve(), curve.getG(), curve.getN(), curve.getH());
	    static final SecureRandom secureRandom = new SecureRandom();
	    static final BigInteger HALF_CURVE_ORDER = curve.getN().shiftRight(1);
	
	
	
	
    public byte[] sign(byte[] hash, byte[] privateKey) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        signer.init(true, new ECPrivateKeyParameters(new BigInteger(privateKey), domain));
        BigInteger[] signature = signer.generateSignature(hash);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DERSequenceGenerator seq = new DERSequenceGenerator(baos);
            seq.addObject(new ASN1Integer(signature[0]));
            seq.addObject(new ASN1Integer(toCanonicalS(signature[1])));
            seq.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
	
    
    private BigInteger toCanonicalS(BigInteger s) {
        if (s.compareTo(HALF_CURVE_ORDER) <= 0) {
            return s;
        } else {
            return curve.getN().subtract(s);
        }
    }
   

    
    
    public boolean verify(byte[] hash, byte[] signature, byte[] publicKey) {
        ASN1InputStream asn1 = new ASN1InputStream(signature);
        try {
            ECDSASigner signer = new ECDSASigner();
            signer.init(false, new ECPublicKeyParameters(curve.getCurve().decodePoint(publicKey), domain));

            DLSequence seq = (DLSequence) asn1.readObject();
            BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
            BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
            return signer.verifySignature(hash, r, s);
        } catch (Exception e) {
            return false;
        } finally {
            try {
                asn1.close();
            } catch (IOException ignored) {
            }
        }
    
    }
    
    
   

	public static void main(String args[]) throws Exception
    {
    	ECCUtil3 ecc=new ECCUtil3();
    	ECCUtil util= new ECCUtil();
		ECPrivateKey ecprivateKey= util.string2PrivateKey("MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgFYZQ4ir0ESKuL8+uz2hexUtAwf4EZUTSwSUm3Ygg/cigCgYIKoZIzj0DAQehRANCAASgSBgJrqJAKGPN7wkZqYI8zvJBfKZx6sTtjfICeutnVFjg0XS4h5I4eh/Nr7LGqh6KirU5enuORj5sKm+OvCHF");   	
        byte[]prk= ecprivateKey.getD().toByteArray();
        
        String ms= "hello how are you?";
        byte[] msg=ms.getBytes();
        byte[]sign = ecc.sign(msg, prk);
		System.out.println(sign.length); 
		System.out.println(HexBin.encode(sign)); 
		System.out.println(HexBin.encode(sign).length()); 
		
	
       
        ECPublicKey ecp=util.string2PublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEoEgYCa6iQChjze8JGamCPM7yQXymcerE7Y3yAnrrZ1RY4NF0uIeSOHofza+yxqoeioq1OXp7jkY+bCpvjrwhxQ==");
        
        byte[] keyBytes = Base64Utils.decodeFromString("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEoEgYCa6iQChjze8JGamCPM7yQXymcerE7Y3yAnrrZ1RY4NF0uIeSOHofza+yxqoeioq1OXp7jkY+bCpvjrwhxQ==");  
        System.out.println("keybytes"+keyBytes);
 
        
        
		boolean bs=ecc.verify(msg, sign, curve.getG().multiply(new BigInteger(prk)).getEncoded(true));
		//boolean b=ecc.verify(msg, sign, ecp.getEncoded() );
        System.out.println(bs);
		


    
    }
    
    
}


