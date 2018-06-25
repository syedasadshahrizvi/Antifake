package com.antifake.service.impl;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.VO.UserVO;
import com.antifake.converter.User2UserVO;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.PrivateKeyMapper;
import com.antifake.mapper.PubKeyMapper;
import com.antifake.mapper.PubKeyRepository;
import com.antifake.mapper.UserMapper;
import com.antifake.model.PrivateKey;
import com.antifake.model.User;
import com.antifake.model.PubKey;
import com.antifake.service.UserService;
import com.antifake.utils.ECCUtil;
import com.antifake.utils.ECCUtilsBak;
import com.antifake.utils.MD5Utils;
import com.antifake.utils.RegExUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PubKeyRepository pubKeyRepository;

	public User findById(String string) {
		User user = userMapper.selectByPrimaryKey(string);
		return user;
	}

	@Override
	public User repetitionByUser(User user) {
		return userMapper.queryUserByUsernameOrTelphone(user);
	}

	@Override
	public Map<String,Object> registUser(User user) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String userId = UUID.randomUUID().toString().replace("-", "");
		user.setUserId(userId);
		user.setPassword(MD5Utils.hash(user.getPassword()));
		userMapper.insertSelective(user);
		//初始化密钥
		KeyPair keyPair = ECCUtil.getKeyPair();  
        String publicKeyStr = ECCUtil.getPublicKey(keyPair);  
        String privateKeyStr = ECCUtil.getPrivateKey(keyPair);
		PubKey pubKey = new PubKey();
		pubKey.setUserId(userId);
		pubKey.setPublicKey(privateKeyStr);
		//保存公钥
		pubKeyRepository.save(pubKey);
		
		UserVO userVO = User2UserVO.converter(user);
		
		resultMap.put("privateKey", publicKeyStr);
		resultMap.put("userVO", userVO);
		
		return resultMap;
	}

	@Override
	public UserVO loginByUsernameOrtelPhone(User userconvert) {

		// 正则匹配手机号
		UserVO userVO = new UserVO();
		if (RegExUtils.isPhone(userconvert.getUsername())) {
			userconvert.setTelphone(userconvert.getUsername());
			userconvert.setUsername(null);
		}
		User user = userMapper.queryUserByUsernameOrTelphone(userconvert);
		if (MD5Utils.hash(userconvert.getPassword()).equals(user.getPassword())) {
			userVO.setUserId(user.getUserId());
			userVO.setNickname(user.getNickname());
			userVO.setTelphone(user.getTelphone());
			userVO.setUsername(user.getUsername());
			userVO.setStatus(user.getStatus());
			userVO.setRoleList(user.getRoleList());
		}

		return userVO;
	}

	@Override
	public UserVO findByTelphone(String telphone) {
		User user = new User();
		user.setTelphone(telphone);
		User userResult = userMapper.queryUserByUsernameOrTelphone(user);
		UserVO userVO = new UserVO();
		if (userResult == null) {
			log.error("【验证码登录】该手机号尚未注册！ telphone = {}", telphone);
			throw new AntiFakeException(ResultEnum.TELPHONE_LOGIN_ERROR);
			/*String userId = UUID.randomUUID().toString().replace("-", "");
			user.setUserId(userId);
			userMapper.insertSelective(user);
			//初始化密钥
			Map<String, String> initKey = ECCUtilsBak.initKey();
			//获取公钥
			String publicKey = ECCUtilsBak.getPublicKey(initKey);
			PubKey pubKey = new PubKey();
			pubKey.setUserId(userId);
			pubKey.setPublicKey(publicKey);
			//保存公钥
			pubKeyRepository.save(pubKey);
			//获取私钥
			String privateKey = ECCUtilsBak.getPrivateKey(initKey);
			userVO.setUserId(userId);
			userVO.setTelphone(telphone);
			userVO.setStatus(1);
			//userVO.setPrivateKey(privateKey);
			PrivateKey privateKeyModel = new PrivateKey();
			privateKeyModel.setUserId(userId);
			privateKeyModel.setPrivateKey(privateKey);
			resultMap.put("privateKey", privateKeyModel);*/
		}else {
			userVO.setUserId(userResult.getUserId());
			userVO.setNickname(userResult.getNickname());
			userVO.setTelphone(userResult.getTelphone());
			userVO.setUsername(userResult.getUsername());
			userVO.setStatus(userResult.getStatus());
			userVO.setRoleList(userResult.getRoleList());
		}
		return userVO;
	}

	@Override
	public Integer updateByUsernameOrTelphone(User userconvert) {
		Integer update = userMapper.updateByPrimaryKeySelective(userconvert);
		return update;
	}

	@Override
	public Boolean updatePwdByPwd(String userId, String oldpwd, String newpwd) {
		User user = userMapper.selectByPrimaryKey(userId);
		if(MD5Utils.hash(oldpwd).equals(user.getPassword())) {
			//更新密码
			user.setPassword(MD5Utils.hash(newpwd));
			userMapper.updateByPrimaryKeySelective(user);
			return true;
		}
		return false;
	}

	@Override
	public Boolean updatePwdByCode(String userId, String newped) {
		User user = new User();
		user.setUserId(userId);
		user.setPassword(newped);
		userMapper.updateByPrimaryKeySelective(user);
		return null;
	}

}
