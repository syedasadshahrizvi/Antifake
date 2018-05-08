package com.antifake.model;

import java.sql.Date;

public class Check {
	
	private Integer checkId;
	
	private String companyCode;
	
	private String productCode;
	
	private String count;
	
	private String code;
	
	private Date queryTime;

	public Integer getCheckId() {
		return checkId;
	}

	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	@Override
	public String toString() {
		return "Check [checkId=" + checkId + ", companyCode=" + companyCode + ", productCode=" + productCode
				+ ", count=" + count + ", code=" + code + ", queryTime=" + queryTime + "]";
	}

}
