package com.antifake.converter;

import com.antifake.form.UserInfoForm;
import com.antifake.model.User;

public class UserInfoForm2UserModelConverter {
	
	public static User convert(UserInfoForm userInfoForm) {
		
		User user = new User();
		
		user.setUsername(userInfoForm.getUsername());
		user.setNickname(userInfoForm.getNickname());
		
		return user;
		
	}

}
