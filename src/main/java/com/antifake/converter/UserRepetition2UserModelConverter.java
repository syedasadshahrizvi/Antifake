package com.antifake.converter;

import com.antifake.form.UserRepetition;
import com.antifake.model.User;

public class UserRepetition2UserModelConverter {
	
	public static User convert(UserRepetition userRepetition) {
		
		User user = new User();
		
		if(userRepetition.getType().equals("username"))
			user.setUsername(userRepetition.getCode());
		if(userRepetition.getType().equals("telphone"))
			user.setTelphone(userRepetition.getCode());
		
		return user;
		
	}

}
