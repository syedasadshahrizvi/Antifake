package com.antifake.model;

public class Product {
	
	private Integer productId;
	
	private Integer companyId;
	
	
	private String productTitle;
	
	private String template;
	
	private String publicKey;
	
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String pk) {
		this.publicKey = pk;
	}
	
	
	

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

	
	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	
	
	

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", companyId=" + companyId +  ", template=" + template + ", public key=" + publicKey+"]";
	}

}
