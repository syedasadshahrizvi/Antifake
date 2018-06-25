package com.antifake.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antifake.mapper.ProductPropertyRepository;
import com.antifake.model.ProductProperty;
import com.antifake.service.ProductPropertyService;

@Service
@Transactional
public class ProductPropertyServiceImpl implements ProductPropertyService{
	
	public static final Integer STATUS = 1;
	public static final Integer DEL_STATUS = 0;
	
	@Autowired
	private ProductPropertyRepository productPropertyRepository;
	
	@Override
	public ProductProperty add(ProductProperty converter) {
		ProductProperty save = productPropertyRepository.save(converter);
		return save;
	}

	@Override
	public ProductProperty edit(ProductProperty converter) {
		ProductProperty productProperty = new ProductProperty();
		productProperty.setPropertyId(converter.getPropertyId());
		Example<ProductProperty> example = Example.of(productProperty);
		
		ProductProperty productProperty2 = productPropertyRepository.findOne(example).get();
		productProperty2.setCode(converter.getCode());
		productProperty2.setName(converter.getName());
		productProperty2.setType(converter.getType());
		ProductProperty save = productPropertyRepository.save(productProperty2);
		return save;
	}

	@Override
	public List<ProductProperty> findAll() {
		ProductProperty productProperty = new ProductProperty();
		productProperty.setStatus(STATUS);
		Example<ProductProperty> example = Example.of(productProperty);
		List<ProductProperty> list = productPropertyRepository.findAll(example);
		return list;
	}

	@Override
	public ProductProperty delete(Integer pid) {
		ProductProperty productProperty = new ProductProperty();
		productProperty.setPropertyId(pid);
		Example<ProductProperty> example = Example.of(productProperty);
		
		ProductProperty productProperty2 = productPropertyRepository.findOne(example).get();
		productProperty2.setStatus(DEL_STATUS);
		ProductProperty save = productPropertyRepository.save(productProperty2);
		return save;
	}

	@Override
	public ProductProperty getById(Integer pid) {
		return productPropertyRepository.findById(pid).get();
	}

}
