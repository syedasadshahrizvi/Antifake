package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Company;

public interface CompanyMapper {
    int deleteByPrimaryKey(Integer companyId);

    int insert(Company record);

    Integer insertSelective(Company record);
    
    Integer insertPKey(@Param("companyPublicKey")String companyPublicKey,@Param("companyId")Integer companyId,@Param("status")Integer status);

    Company selectByPrimaryKey(Integer companyId);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

	Integer selectByRegisterId(Company record);
	
	Company selectCompanyByRegisterId(@Param("registerId")String registerId);

	List<Company> selectByUserId(@Param("userId")String userId);

	List<Company> selectList(@Param("status")Integer status, @Param("userId")String userId);
}