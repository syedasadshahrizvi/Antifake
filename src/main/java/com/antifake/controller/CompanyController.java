package com.antifake.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.CompanyForm;
import com.antifake.interceptor.LoginRequired;
import com.antifake.model.Company;
import com.antifake.service.CompanyService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/company")
@Slf4j
@CrossOrigin
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	
	/**
	 * <p>Description: 申请公司角色</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@PostMapping("/add")
	//@LoginRequired
	public ResultVO<Map<String,Object>> applyCompany(@Valid @RequestBody CompanyForm companyForm, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			log.error("【申请公司角色】请求参数有误, companyFore = {}", companyForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		
		Company converteCompany = CompanyForm2CompanyModelConverter.converte(companyForm);
		Company company = companyService.addCompany(converteCompany);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("companyId", company.getCompanyId());
		return ResultVOUtil.success(resultMap);
	}
	
	
	
	
	@GetMapping("/newCompanyKey/{id}")
	public void newCompanyKey( @PathVariable(name= "id") int id, HttpServletResponse res ){
		
		HashMap<String, Object> map;
		String cer;
		try {
			map = companyService.addCompanyKey(id);
		} catch (Exception e) {
			
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), "add companyPublickeyError");
		}
		Map<String,Object> resultMap = new HashMap<>();
		cer=(String) map.get("2");
	
		resultMap.put("Certificate", cer);
	
		resultMap.put("PublicKey", map.get("1"));
		
		resultMap.put("CompanyId", id);
	
		
		try {
			
			companyService.saveCertificate(cer,id);
			
			companyService.downloadCertificate(res,id);
			
			companyService.deleteCertificate(id);
			System.out.println("success");
		} catch (Exception e) {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), "saving Certifacate Error");
		}
		//return ResultVOUtil.success(resultMap);
	}
	/**
	 * <p>Description: 查询用户名下的公司</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@GetMapping("/list/{userId}")
	public ResultVO<List<Company>> listByUserId(@PathVariable(name = "userId")String userId){
		List<Company> companyList = companyService.selectCompanyByUserid(userId);
		return ResultVOUtil.success(companyList);
	}
	
	/**
	 * <p>Description: 调整公司级别</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@PostMapping("/changelevel/{companyId}/{level}")
	public ResultVO changeLevel(@PathVariable(name="companyId") Integer companyId,@PathVariable(name="level") Byte level){
		Company company = new Company();
		company.setCompanyId(companyId);
		company.setLevel(level);
		companyService.changeLevel(company);
		return ResultVOUtil.success();
	}
	
	/**
	 * <p>Description: 更新公司信息</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@PostMapping("/update")
	public ResultVO<Map<String,Object>> changeCompany(@Valid @RequestBody CompanyForm companyForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors() || companyForm.getCompanyId()==null ) {
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		Company converteCompany = CompanyForm2CompanyModelConverter.converte(companyForm);
		converteCompany.setCompanyId(companyForm.getCompanyId());
		converteCompany.setLevel(null);
		Company company = companyService.updateCompany(converteCompany);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("company", company);
		return ResultVOUtil.success();
	}
	
	/**
	 * <p>Description: 条件查询公司账户</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@GetMapping("/all")
	public ResultVO<List<Company>> listCompany(@RequestParam(required=false) Integer status,@RequestParam(required=false) String userId){
		List<Company> companyList = companyService.selectCompanyList(status,userId);
		return ResultVOUtil.success(companyList);
	}
	
	/**
	 * <p>Description: 审核公司</p>
	 * @author JZR  
	 * @date 2018年4月12日 
	 */
	@GetMapping("/check/{companyId}/{status}")
	public ResultVO checkCompany(@PathVariable(name="companyId")Integer companyId,@PathVariable(name="status")Byte status) {
		Company company = new Company();
		company.setCompanyId(companyId);
		company.setStatus(status);
		companyService.updateCompany(company);
		return ResultVOUtil.success();
	}
	
}
