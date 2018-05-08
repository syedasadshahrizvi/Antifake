package com.antifake.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtils {
	
	public static String phoneRegEx = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
	
	public static Boolean isPhone(String telphone){
		Pattern pattern = Pattern.compile(phoneRegEx);
		Matcher matcher = pattern.matcher(telphone);
		return matcher.matches();
	}
	
}
