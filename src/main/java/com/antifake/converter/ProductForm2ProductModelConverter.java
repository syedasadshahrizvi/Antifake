package com.antifake.converter;


import com.antifake.form.ProductForm;

import com.antifake.model.Product;;

public class ProductForm2ProductModelConverter {

public static Product converte(ProductForm productForm) {
		
		Product product = new Product();
		
		
		product.setCompanyId(productForm.getCompanyId());
		product.setTemplate(productForm.getTemplate());
		product.setProductTitle(productForm.getProductTitle());
		
		
		return product;
	}
}
