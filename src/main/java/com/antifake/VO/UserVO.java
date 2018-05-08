package com.antifake.VO;

import lombok.Data;

@Data
public class UserVO {
	
	/**	用户名.	*/
	private String username;
	
	/**	用户身份状态	*/
	private Byte status;
	
	/**	手机号	*/
	private String telphone;
	
	/**	昵称		*/
	private String nickname;
	
	/**	是否实名制	*/
	private Byte realStatus;
	
	/**	私钥	*/
	private String privateKey;

}
