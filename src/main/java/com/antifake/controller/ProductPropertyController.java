package com.antifake.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.converter.ProductPropertyForm2ProductProperty;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.ProductPropertyForm;
import com.antifake.model.ProductProperty;
import com.antifake.service.ProductPropertyService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product/property")
@Slf4j
public class ProductPropertyController {
	
	@Autowired
	private ProductPropertyService productPropertyService;
	
	
	/**
	  * <p>Description: 添加模板</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	@PostMapping("/add")
	public ResultVO add(@Valid ProductPropertyForm propertyForm,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			log.error("【添加模板】添加模板参数不正确，propertyForm={}", propertyForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		ProductProperty converter = ProductPropertyForm2ProductProperty.converter(propertyForm);
		productPropertyService.add(converter);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 修改模板</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	@PostMapping("/edit")
	public ResultVO	edit(@Valid ProductPropertyForm propertyForm,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors() || propertyForm.getPropertyId()==null) {
			log.error("【添加模板】添加模板参数不正确，propertyForm={}", propertyForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), propertyForm.getPropertyId()==null?ResultEnum.ID_ERROR.getMessage():bindingResult.getFieldError().getDefaultMessage());
		}
		
		ProductProperty converter = ProductPropertyForm2ProductProperty.converter(propertyForm);
		productPropertyService.edit(converter);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 查询模板</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	@GetMapping("/list")
	public ResultVO<List<ProductProperty>> list(){
		List<ProductProperty> list = productPropertyService.findAll();
		return ResultVOUtil.success(list);
	}
	
	/**
	  * <p>Description: 删除模板属性</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	@GetMapping("/delete/{pid}")
	public ResultVO delete(@PathVariable(name="pid")Integer pid) {
		productPropertyService.delete(pid);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 根据Id查询</p>
	  * @author JZR  
	  * @date 2018年5月18日
	  */
	@GetMapping("/get/{pid}")
	public ResultVO<ProductProperty> get(@PathVariable(name="pid")Integer pid){
		ProductProperty productProperty = productPropertyService.getById(pid);
		return ResultVOUtil.success(productProperty);
	}
}
