package com.antifake.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.mapper.RoleMapper;
import com.antifake.model.Role;
import com.antifake.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> queryByUId(String tokenValue) {
		return roleMapper.queryByUid(tokenValue);
	}

}
