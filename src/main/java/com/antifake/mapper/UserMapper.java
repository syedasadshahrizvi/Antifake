package com.antifake.mapper;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	/**
	 * <p>Description: 根据账户或手机号查询用户</p>
	 * @author JZR  
	 * @date 2018年4月10日 
	 */
	User queryUserByUsernameOrTelphone(@Param("user")User user);

	/**
	  * <p>Description: 添加用户角色对应关系</p>
	  * @author JZR  
	  * @date 2018年5月8日
	  */
	Integer saveUserRole(@Param("userId")String userId,@Param("roleId")Integer roleId);

	/**
	  * <p>Description: 查询对应关系</p>
	  * @author JZR  
	  * @date 2018年5月8日
	  */
	Integer queryUserRole(@Param("userId")String userId, @Param("roleId")Integer roleId);

}