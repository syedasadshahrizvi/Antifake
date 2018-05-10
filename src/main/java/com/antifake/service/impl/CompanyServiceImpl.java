package com.antifake.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.UserMapper;
import com.antifake.model.Company;
import com.antifake.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService{
	
	public static final Integer roleId = 2;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public Company addCompany(Company converteCompany) {
		
		//查询公司是否重复
		Integer count = companyMapper.selectByRegisterId(converteCompany);
		if(count>0) {
			throw new AntiFakeException(ResultEnum.COMPANY_REPEAT);
		}
		//创建公司
		companyMapper.insertSelective(converteCompany);
		//添加公司角色关联关系
		Integer flag = userMapper.queryUserRole(converteCompany.getUserId(),roleId);
		if(flag<=0)
			userMapper.saveUserRole(converteCompany.getUserId(),roleId);
		return converteCompany;
	}

	@Override
	public List<Company> selectCompanyByUserid(String userId) {
		List<Company> companyList = companyMapper.selectByUserId(userId);
		return companyList;
	}

	@Override
	public void changeLevel(Company company) {
		companyMapper.updateByPrimaryKeySelective(company);
	}

	@Override
	public Company updateCompany(Company converteCompany) {
		companyMapper.updateByPrimaryKeySelective(converteCompany);
		return converteCompany;
	}

	@Override
	public List<Company> selectCompanyList(Integer status, String userId) {
		List<Company> companyList = companyMapper.selectList(status,userId);
		return companyList;
	}

}
