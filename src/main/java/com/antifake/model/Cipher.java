package com.antifake.model;

import java.sql.Date;

public class Cipher {
	
	private Integer cipherId;
	
	/**	公司主键*/
	private Integer companyId;

	
	private Integer productId;
	
	/**	密文*/
	private String cipherText;
	
	/**	唯一码*/
	private String code;
	
	/**	查询次数*/
	private String count;
	
	/**	（内部）批次*/
	private String batch;
	
	/**	是否作废*/
	private String valid;
	
	/**	创建时间*/
	private String createTime;
	
	/**	初次查询时间*/
	private Date queryTime;
	
	/**	追溯预留*/
	private String	ascend;

	public Integer getCipherId() {
		return cipherId;
	}

	public void setCipherId(Integer cipherId) {
		this.cipherId = cipherId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public String getAscend() {
		return ascend;
	}

	public void setAscend(String ascend) {
		this.ascend = ascend;
	}

	@Override
	public String toString() {
		return "Cipher [cipherId=" + cipherId + ", companyId=" + companyId + 
				 ", productId=" + productId + ", cipherText=" + cipherText + ", code=" + code + ", count=" + count
				+ ", batch=" + batch + ", valid=" + valid + ", createTime=" + createTime + ", queryTime=" + queryTime
				+ ", ascend=" + ascend + "]";
	}
	
	
}
