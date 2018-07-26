package com.antifake.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.mapper.CompanyMapper;
import com.antifake.mapper.ProductMapper;
import com.antifake.model.Company;
import com.antifake.model.Product;
import com.antifake.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
	
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CompanyMapper companyMapper;
	

	@Override
	public Integer createProduct(Product product) {
		//查询公司状态
		Company company = companyMapper.selectByPrimaryKey(product.getCompanyId());
		if(company==null || company.getStatus()==0) {
			throw new AntiFakeException(ResultEnum.COMPANY_ERROR.getCode(), ResultEnum.COMPANY_ERROR.getMessage());
		}
		Integer flag = productMapper.createProduct(product);
		return flag;
	}


	@Override
	public Product getProductBypId(Integer productId) {
		return productMapper.queryByProductId(productId);
	}

	@Override
	public List<Product> getProductList(Integer companyId) {
		return productMapper.queryByCId(companyId);
	}
}
