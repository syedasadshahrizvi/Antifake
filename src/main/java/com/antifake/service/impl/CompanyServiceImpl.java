package com.antifake.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.UserMapper;
import com.antifake.model.Company;
import com.antifake.service.CompanyService;
import com.antifake.utils.ECCUtil;

@Service
@Transactional
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
	
	@Override
	public void saveCertificate(String cer,int id) throws Exception   {
		
		 String certPath = "./src/main/resources/cert/"+ id +".cer";  
		 File file = new File(certPath);
	     FileWriter fw = new FileWriter(file);
	     System.out.println(cer);
	     fw.write(cer);
	     fw.flush();   
	     fw.close(); 
		
	}
	public void downloadCertificate(HttpServletResponse res,int id) throws Exception   {
		
		String fileName =""+id+".cer";
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File("./src/main/resources/cert/"+fileName)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    }

		
	     
	       
	 
	
	public void deleteCertificate(int id) throws Exception   {
		
		

    		
    		File file = new File("./src/main/resources/cert/"+id+".cer");
        	
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}
    	
    
	       
	     
	       
		 
	}
	@Override
	public HashMap<String, Object> addCompanyKey(int id) throws Exception  {
		
		
		HashMap<String, Object> map = ECCUtil.getPublickey();
		String pkey=(String)map.get("1");
		String cert=(String)map.get("2");
		
		
		companyMapper.insertPKey(pkey, id,0);
		
		return map;
	}

}
