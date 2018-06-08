package com.antifake.service.impl;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.antifake.VO.UserVO;
import com.antifake.converter.User2UserVO;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.PubKeyMapper;
import com.antifake.mapper.PubKeyRepository;
import com.antifake.mapper.UserMapper;
import com.antifake.model.PubKey;
import com.antifake.model.User;
import com.antifake.service.KeyService;
import com.antifake.utils.ECCUtil;

@Service
public class KeyServiceImpl implements KeyService{
	
	public static final Integer STATUS = 1;
	public static final Integer DEL_STATUS = 0;
	
	@Autowired
	private PubKeyRepository pubKeyRepository;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public String createKey(String userId) throws Exception {
		KeyPair keyPair = ECCUtil.getKeyPair();
		String privateKey = ECCUtil.getPrivateKey(keyPair);
		String publicKey = ECCUtil.getPublicKey(keyPair);
		/*****创建证书******/
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
	        
	        String certPath = "d:/jzr.cer";  
	        
	        
	        FileOutputStream fos = new FileOutputStream(certPath);  
	        fos.write(cert.getEncoded());  
	        fos.close(); 
		
		
		PubKey pubKey = new PubKey();
		pubKey.setUserId(userId);
		pubKey.setPublicKey(privateKey);
		
		pubKeyRepository.save(pubKey);
		
		return publicKey;
	}

	@Override
	public List<PubKey> getPubKey(String uid) {
		PubKey pubKey = new PubKey();
		pubKey.setUserId(uid);
		pubKey.setStatus(STATUS);
		Example<PubKey> example = Example.of(pubKey);
		List<PubKey> pubKeyList = pubKeyRepository.findAll(example);
		return pubKeyList;
	}

	@Override
	public UserVO getUser(String publicKey) {
		PubKey pubKey = new PubKey();
		pubKey.setPublicKey(publicKey);
		Example<PubKey> example = Example.of(pubKey);
		Optional<PubKey> findOne = pubKeyRepository.findOne(example);
		
		if(!findOne.isPresent()) {
			throw new AntiFakeException(ResultEnum.PUB_KEY_NOT_EXIST);
		}
		PubKey ResultPubKey = findOne.get();
		User user = userMapper.selectByPrimaryKey(ResultPubKey.getUserId());
		if(user==null) {
			throw new AntiFakeException(ResultEnum.USER_NOT_EXIST);
		}
		UserVO userVO = User2UserVO.converter(user);
		
		return userVO;
	}

	@Override
	public PubKey replacePubKey(String oldPubKey, String newPubKey) {
		PubKey pubKey = new PubKey();
		pubKey.setPublicKey(oldPubKey);
		Example<PubKey> example = Example.of(pubKey);
		Optional<PubKey> findOne = pubKeyRepository.findOne(example);
		if(!findOne.isPresent()) {
			throw new AntiFakeException(ResultEnum.PUB_KEY_NOT_EXIST);
		}
		PubKey resultPubKey = findOne.get();
		resultPubKey.setPublicKey(newPubKey);
		return pubKeyRepository.save(resultPubKey);
	}

	@Override
	public PubKey delete(String publicKey) {
		PubKey pubKey = new PubKey();
		pubKey.setPublicKey(publicKey);
		Example<PubKey> example = Example.of(pubKey);
		Optional<PubKey> findOne = pubKeyRepository.findOne(example);
		if(!findOne.isPresent()) {
			throw new AntiFakeException(ResultEnum.PUB_KEY_NOT_EXIST);
		}
		PubKey resultPubKey = findOne.get();
		resultPubKey.setStatus(DEL_STATUS);
		return pubKeyRepository.save(resultPubKey);
	}

}
