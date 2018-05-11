package com.antifake.service.impl;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antifake.mapper.PubKeyMapper;
import com.antifake.model.PubKey;
import com.antifake.service.KeyService;
import com.antifake.utils.ECCUtil;

@Service
public class KeyServiceImpl implements KeyService{
	
	@Autowired
	private PubKeyMapper userKeyMapper;

	@Override
	public String createKey(String userId) throws Exception {
		KeyPair keyPair = ECCUtil.getKeyPair();
		String privateKey = ECCUtil.getPrivateKey(keyPair);
		String publicKey = ECCUtil.getPublicKey(keyPair);
		
		PubKey userKey = new PubKey();
		userKey.setUserId(userId);
		userKey.setPublicKey(privateKey);
		
		userKeyMapper.insertUserKey(userKey);
		
		return publicKey;
	}

}
