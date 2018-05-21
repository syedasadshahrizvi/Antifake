package com.antifake.service;

import java.util.List;

import com.antifake.VO.UserVO;
import com.antifake.model.PubKey;

public interface KeyService {

	/**
	  * <p>Description: 生成秘钥</p>
	  * @author JZR  
	  * @date 2018年5月10日
	  */
	String createKey(String userId) throws Exception;

	/**
	  * <p>Description: 根据用户id查询公钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	List<PubKey> getPubKey(String uid);

	/**
	  * <p>Description: 根据公钥查询用户</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	UserVO getUser(String pubKey);

	/**
	  * <p>Description: 替换公钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	PubKey replacePubKey(String oldPubKey, String newPubKey);

	/**
	  * <p>Description: 删除公钥</p>
	  * @author JZR  
	  * @date 2018年5月21日
	  */
	PubKey delete(String pubKey);

}
