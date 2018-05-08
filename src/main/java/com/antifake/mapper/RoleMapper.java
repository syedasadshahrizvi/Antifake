package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

	List<Role> queryByUid(@Param("uid")String uid);
}