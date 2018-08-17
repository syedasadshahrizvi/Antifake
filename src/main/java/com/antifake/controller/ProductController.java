package com.antifake.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.converter.CompanyForm2CompanyModelConverter;
import com.antifake.converter.ProductForm2ProductModelConverter;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.CompanyForm;
import com.antifake.form.ProductForm;
import com.antifake.form.ProductForm1;
import com.antifake.model.Company;
import com.antifake.model.Product;
import com.antifake.service.ProductService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Slf4j
@CrossOrigin
public class ProductController {
	 
	@Autowired
	private ProductService productService;
	
	/**
	  * <p>Description: 创建商品模板</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	/*@PostMapping("/create")
	public ResultVO createProduct(@RequestParam("companyId")Integer companyId,@RequestParam("companyCode")String companyCode,@RequestParam("productCode")String productCode,@RequestParam("template")String template,@RequestParam("productTitle")String productTitle ) {
		productService.createProduct(companyId,companyCode,productCode,template,productTitle);
		return ResultVOUtil.success();
	}*/
	
	@PostMapping("/create")
	public ResultVO createProduct(@Valid @RequestBody ProductForm productForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			log.error("【申请公司角色】请求参数有误, companyFore = {}", productForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		
		Product converteProduct = ProductForm2ProductModelConverter.converte(productForm);
		productService.createProduct(converteProduct);
	
		return ResultVOUtil.success();
	}
	
	
	
	
	/**
	  * <p>Description: 查询公司商品模板列表</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/getlist/{companyId}")
	public ResultVO<List<Product>> listProduct(@PathVariable("companyId")Integer companyId){
		List<Product> productList = productService.getProductList(companyId);
		return ResultVOUtil.success(productList);
	}
	
	@GetMapping("/getlist1/{companyId}")
	public ResultVO<List<Product>> listProduct1(@PathVariable("companyId")Integer companyId){
		List<ProductForm1> productList = productService.getProductList1(companyId);
		
		
		
		return ResultVOUtil.success(productList);
	}
	
	
	
	
	/**
	  * <p>Description: 获取商品模板</p> 
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/get/{productId}")
	public ResultVO<Product> getProduct(@PathVariable("productId")Integer productId){
		Product product = productService.getProductBypId(productId);
		return ResultVOUtil.success(product);
	}
	@GetMapping("/getdetails/{productId}")
	public ResultVO<Product> getProductDretails(@PathVariable("productId")Integer productId){
		Product product = productService.getProductBypId(productId);
		
		Map m= new HashMap<String,Object>();
		m.put("productId", product.getProductId());
		m.put("template", product.getTemplate());
		
		return ResultVOUtil.success(m);
	}
	
}
