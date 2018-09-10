package com.antifake.model;

import java.sql.Date;


public class Code {
	
	private String codeId;
	
	/**	公司主键*/
	

	
	private Integer productId;
	
	/**	密文*/
	private String signature;
	
	private Integer queryTimes;
	
	private Integer status;
	
	private Date firstQueryTime;
	
	private String prodcedDay;
	
	public String getProdcedDay() {
		
		return prodcedDay.toString();
	}

	public void setProdcedDay(String date) {
		
		
		this.prodcedDay =  date;
		//System.out.println(prodcedDay);
	}

	
	
	public Date getFirstQueryTime() {
		return firstQueryTime;
	}

	public void setFirstQueryTime(Date firstQueryTime) {
		this.firstQueryTime = firstQueryTime;
	}

	
	
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

	public Integer getQueryTimes() {
		return queryTimes;
	}

	public void setQueryTimes(Integer queryTime) {
		this.queryTimes = queryTime;
	}


	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	

	
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	
	@Override
	public String toString() {
		return "Cipher [codeId=" + codeId  + 
				 ", productId=" + productId + ", signature=" + signature + ","
				+ ", status=" + status + ", queryTimes=" + queryTimes + ", prodced_day=" + prodcedDay
				 + "]";
	}
	
	
}
