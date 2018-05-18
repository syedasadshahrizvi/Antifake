package com.antifake.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Table(name="company_product_property")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ProductProperty {
	
	/**	id*/
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer propertyId;
	
	/**	编码*/
	private String code;
	
	/**	名称*/
	private String name;
	
	/**	类型*/
	private String type;
	
	/**	状态*/
	private Integer status = 1;
	
	/**	创建时间*/
	@CreatedDate
	private Date createTime;
}
