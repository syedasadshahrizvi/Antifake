package com.antifake.mapper;

import java.util.List;

import com.antifake.model.UserKey;

public interface UserKeyMapper {
	
	Integer insertUserKey(UserKey userKey);

	/**
	  * <p>Description: 根据用户id查询用户公钥</p>
	  * @author JZR  
	  * @date 2018年4月24日
	  */
	List<UserKey> selectKeyByUid(String userId);
	
}
