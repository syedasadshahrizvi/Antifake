package com.antifake.model;

public class Product {
	
	private Integer productId;
	
	private Integer companyId;
	
	private String companyCode;
	
	private String productCode;
	private String productTitle;
	
	private String template;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	
	
	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	
	
	
	

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", companyId=" + companyId + ", companyCode=" + companyCode
				+ ", productCode=" + productCode + ", template=" + template + "]";
	}

}
