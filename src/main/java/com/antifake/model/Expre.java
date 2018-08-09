package com.antifake.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Expre {
	
	private Integer expreId;
	
	/**公司主键*/
	private Integer companyId;
	
	/**明文*/
	private String productExpre;
	
	/**(内部)批次*/
	private String batch;
	
	/**	创建时间*/
	private String createTime;

	public Integer getExpreId() {
		return expreId;
	}

	public void setExpreId(Integer expreId) {
		this.expreId = expreId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getProductExpre() {
		return productExpre;
	}

	public void setProductExpre(String productExpre) {
		this.productExpre = productExpre;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp date) {
	
		Date dates = new Date(date.getTime());
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm").format(dates);
		this.createTime = timeStamp;
		
	}

	@Override
	public String toString() {
		return "Expre [expreId=" + expreId + ", companyId=" + companyId + ", productExpre=" + productExpre + ", batch="
				+ batch + ", createTime=" + createTime + "]";
	}

}
