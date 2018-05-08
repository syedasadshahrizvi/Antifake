package com.antifake.converter;

import com.antifake.form.UserRegistForm;
import com.antifake.model.User;

public class UserForm2UserModelConverter {
	
	public static User convert(UserRegistForm userForm) {
		
		User user = new User();
		
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.setTelphone(userForm.getTelphone());
		
		return user;
		
	}

}
