package com.antifake.VO;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ExpreVO {
	
	private Integer expreId;
	
	/**公司主键*/
	private Integer companyId;
	
	/**明文*/
	private String productExpre;
	
	/**(内部)批次*/
	private String batch;
	
	/**	总量 */
	private Integer total;
	
	/**	用量	*/
	private Integer use;
	
	/**	创建时间*/
	private Date createTime;
}
