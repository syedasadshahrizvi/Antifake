package com.antifake.service;

import java.util.Map;

import com.antifake.VO.UserVO;
import com.antifake.model.User;

public interface UserService {

	User findById(String string);

	/**
	 * <p>Description: 根据用户参数查找用户是否存在</p>
	 * @author JZR  
	 * @date 2018年4月10日 
	 */
	User repetitionByUser(User user);

	/**
	 * <p>Description: 用户注册</p>
	 * @author JZR  
	 * @date 2018年4月10日 
	 */
	Map<String,Object> registUser(User userconvert) throws Exception;

	/**
	 * <p>Description: 用户名或手机号登陆</p>
	 * @author JZR  
	 * @date 2018年4月10日 
	 */
	UserVO loginByUsernameOrtelPhone(User userconvert);

	/**
	 * <p>Description: 根据手机号查询用户信息（如果用户不存在则新建用户）</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	UserVO findByTelphone(String telphone);

	/**
	 * <p>Description: 根据账户或手机号修改用户信息</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	UserVO updateByUsernameOrTelphone(User userconvert);

	/**
	 * <p>Description: 修改密码（根据密码）</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	Boolean updatePwdByPwd(String userId, String oldpwd, String newpwd);

	/**
	 * <p>Description:  修改密码（验证码方式）</p>
	 * @author JZR  
	 * @date 2018年4月11日 
	 */
	Boolean updatePwdByCode(String userId,String newped);

}
