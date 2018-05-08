package com.antifake.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.model.Product;
import com.antifake.service.ProductService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/**
	  * <p>Description: 创建商品模板</p>
	  * @author JZR  
	  * @date 2018年4月17日
	  */
	@PostMapping("/create")
	public ResultVO createProduct(@RequestParam("companyId")Integer companyId,@RequestParam("companyCode")String companyCode,@RequestParam("productCode")String productCode,@RequestParam("template")String template) {
		productService.createProduct(companyId,companyCode,productCode,template);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 查询公司商品模板列表</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/getlist")
	public ResultVO<List<Product>> listProduct(@RequestParam("companyId")Integer companyId){
		List<Product> productList = productService.getProductList(companyId);
		return ResultVOUtil.success(productList);
	}
	
	/**
	  * <p>Description: 获取商品模板</p> 
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/get")
	public ResultVO<Product> getProduct(@RequestParam("productId")Integer productId){
		Product product = productService.getProductBypId(productId);
		return ResultVOUtil.success(product);
	}
	
	/**
	  * <p>Description: 生成防伪码</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@PostMapping("/createcode")
	public ResultVO<List<String>> createCode(String privateKey,String companyCode,String productCode,String template,Integer count) {
		List<String> list = productService.encrypt(privateKey,companyCode,productCode,template,count);
		return ResultVOUtil.success(list);
	}
	
	/**
	  * <p>Description: 校验防伪码</p>
	  * @author JZR  
	  * @date 2018年5月7日
	  */
	@GetMapping("/checked")
	public ResultVO<Map<String,Object>> checkCode(@RequestParam("codeString")String codeString,@RequestParam(name="type",required = false)String type) throws Exception {
		Map<String,Object> resultMap = productService.checkCode(codeString,type);
		return ResultVOUtil.success(resultMap);
	}
}
