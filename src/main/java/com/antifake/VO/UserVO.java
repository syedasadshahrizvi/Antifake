package com.antifake.VO;

import java.util.List;

import com.antifake.model.Role;

import lombok.Data;

@Data
public class UserVO {
	
	/**	用户Id.	*/
	private String userId;
	
	/**	用户名.	*/
	private String username;
	
	/**	用户身份状态	*/
	private Integer status;
	
	/**	手机号	*/
	private String telphone;
	
	/**	昵称		*/
	private String nickname;
	
	/**	是否实名制	*/
	private Byte realStatus;
	
	/**	私钥	*/
	//private String privateKey;
	
	/**	角色	*/
	private List<Role> roleList;

}
