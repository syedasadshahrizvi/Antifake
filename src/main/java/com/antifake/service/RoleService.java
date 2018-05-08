package com.antifake.service;

import java.util.List;

import com.antifake.model.Role;

public interface RoleService {

	List<Role> queryByUId(String tokenValue);

}
