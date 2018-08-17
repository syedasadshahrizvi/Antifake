package com.antifake.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.ProductForm1;
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
	
	@Override
	public List<ProductForm1> getProductList1(Integer companyId)
	{
		List<Product> p=productMapper.queryByCId(companyId);
		List<ProductForm1> pf= new ArrayList<ProductForm1>();
		
		for(Product p1:p)
		{
			ProductForm1 pf1=new ProductForm1();
			pf1.setProductId(p1.getProductId());
			pf1.setProductTitle(p1.getProductTitle());
			
			pf.add(pf1);
		}
		
		return pf;
		
	}
	
	
}
