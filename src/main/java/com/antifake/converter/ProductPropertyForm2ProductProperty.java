package com.antifake.converter;

import com.antifake.form.ProductPropertyForm;
import com.antifake.model.ProductProperty;

public class ProductPropertyForm2ProductProperty {
	
	public static ProductProperty converter(ProductPropertyForm propertyForm) {
		ProductProperty productProperty = new ProductProperty();
		
		productProperty.setCode(propertyForm.getCode());
		productProperty.setName(propertyForm.getName());
		productProperty.setType(propertyForm.getType());
		
		return productProperty;
	}
}
