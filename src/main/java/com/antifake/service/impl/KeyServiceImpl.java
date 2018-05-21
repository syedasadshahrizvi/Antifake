package com.antifake.service.impl;

import java.security.KeyPair;
import java.util.List;
import java.util.Optional;

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
	private PubKeyMapper userKeyMapper;
	
	@Autowired
	private PubKeyRepository pubKeyRepository;
	
	@Autowired
	private UserMapper userMapper;

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
