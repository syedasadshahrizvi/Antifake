package com.antifake.model;

public class Cipher {
	
	private Integer cipherId;
	private String companyCode;
	private String productCode;
	private String cipherText;
	private String code;
	public Integer getCipherId() {
		return cipherId;
	}
	public void setCipherId(Integer cipherId) {
		this.cipherId = cipherId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getCipherText() {
		return cipherText;
	}
	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Cipher [cipherId=" + cipherId + ", companyCode=" + companyCode + ", productCode=" + productCode
				+ ", cipherText=" + cipherText + ", code=" + code + "]";
	}
	
}
