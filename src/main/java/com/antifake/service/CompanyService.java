package com.antifake.service;

import java.util.List;
import java.util.Map;

import com.antifake.model.Company;

public interface CompanyService {

	/**
	 * <p>Description: 添加公司</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	Company addCompany(Company converteCompany);

	/**
	 * <p>Description: 根据用户id查询公司</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	List<Company> selectCompanyByUserid(String userId);

	/**
	 * <p>Description: 跟新公司级别</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	void changeLevel(Company company);

	/**
	 * <p>Description: 更新公司信息</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	Company updateCompany(Company converteCompany);

	/**
	 * <p>Description: 条件查询公司</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	List<Company> selectCompanyList(Integer status, String userId);

}
