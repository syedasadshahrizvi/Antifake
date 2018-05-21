package com.antifake.converter;

import com.antifake.VO.UserVO;
import com.antifake.model.User;

public class User2UserVO {
	
	public static UserVO converter(User user) {
		UserVO userVO = new UserVO();
		userVO.setUserId(user.getUserId());
		userVO.setUsername(user.getUsername());
		userVO.setNickname(user.getNickname());
		userVO.setTelphone(user.getTelphone());
		return userVO;
	}

}
