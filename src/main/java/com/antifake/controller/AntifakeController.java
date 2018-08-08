package com.antifake.controller;

import java.util.List;
import java.util.Map;

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

import com.antifake.VO.ExpreVO;
import com.antifake.VO.ResultVO;
import com.antifake.converter.AntifakeForm2AntifakeModelConverter;
import com.antifake.converter.CompanyForm2CompanyModelConverter;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.AntifakeForm;
import com.antifake.form.CheckedForm;
import com.antifake.form.CompanyForm;
import com.antifake.model.Antifake;
import com.antifake.model.Cipher;
import com.antifake.model.Company;
import com.antifake.service.AntifakeService;
import com.antifake.service.AntifakeService2;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/antifake")
@Slf4j
@CrossOrigin
public class AntifakeController {

	@Autowired
	private AntifakeService antifakeService;
	@Autowired
	private AntifakeService2 antifakeService2;


	/**
	 * <p>
	 * Description: 生成防伪码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年5月7日
	 */
	@PostMapping("/create")
	public ResultVO<List<String>> createCipher(@RequestBody AntifakeForm antifakeFrom) {
		List<String> list = antifakeService.encrypt(antifakeFrom.getPrivateKey(), antifakeFrom.getCompanyId(), antifakeFrom.getProductId(), antifakeFrom.getTemplate() , antifakeFrom.getCount());
		return ResultVOUtil.success(list);
	}
	
	@PostMapping("/create2")
	public ResultVO<List<String>> createCipher2(@RequestBody AntifakeForm antifakeFrom) {
		List<String> list = antifakeService.encrypt2(antifakeFrom.getPrivateKey(), antifakeFrom.getCompanyId(), antifakeFrom.getProductId(), antifakeFrom.getTemplate(), antifakeFrom.getCount());
		return ResultVOUtil.success(list);
	}

	
	
	@PostMapping("/sign")
	public ResultVO<Boolean> signCipher(@RequestBody AntifakeForm antifakeFrom) throws Exception {
	List<String> sign = antifakeService2.sign(antifakeFrom.getPrivateKey(), antifakeFrom.getCompanyId(), antifakeFrom.getProductId(), antifakeFrom.getTemplate() );
	//System.out.println(sign);
		return ResultVOUtil.success(sign);
	}
	
	@PostMapping("/verify")
	public ResultVO<Boolean> verifyCipher(@RequestBody CheckedForm checkedForm) throws Exception {
		
		Map<String, Object> resultMap= antifakeService2.verify(checkedForm.getCodeString(),  checkedForm.getType());
	
		return ResultVOUtil.success(resultMap);
	}
	
	
	/**
	 * <p>
	 * Description: 校验防伪码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年5月7日
	 */
	/*@GetMapping("/checked/{codeString}/{type}")
	public ResultVO<Map<String, Object>> checkCipher(@PathVariable("codeString") String codeString,
			@PathVariable(name = "type") String type) throws Exception {
		Map<String, Object> resultMap = antifakeService.checkCode(codeString, type);
		return ResultVOUtil.success(resultMap);
	}*/
	
	@PostMapping("/checked")
	public ResultVO<Map<String, Object>> checkCipher(@RequestBody CheckedForm checkedForm) throws Exception {
		Map<String, Object> resultMap = antifakeService.checkCode(checkedForm.getCodeString(), checkedForm.getType());
		return ResultVOUtil.success(resultMap);
	}
	

	/**
	 * <p>
	 * Description: 单个作废/召回防伪码
	 * </p>
	 * 
	 * @author JZR
	 * @throws Exception
	 * @date 2018年5月28日
	 */
	/*@PostMapping("/update")
	public ResultVO downCipher(@RequestParam("cipherId") Integer cipherId, 
							   @RequestParam("companyId") Integer companyId,
							   @RequestParam("privateKey") String privateKey,
							   @RequestParam("status") String status) throws Exception {
		antifakeService.updateCipher(cipherId, companyId, privateKey,status);
		return ResultVOUtil.success();
	}*/

	@PostMapping("/update")
	public ResultVO downCipher(@Valid @RequestBody AntifakeForm antifakeForm, BindingResult bindingResult) throws Exception {
		
		if(bindingResult.hasErrors()) {
			log.error("【申请公司角色】请求参数有误, companyFore = {}", antifakeForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		Antifake converteAntifake = AntifakeForm2AntifakeModelConverter.converte(antifakeForm);
		 antifakeService.updateCipher(converteAntifake);
		
				return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 按批次批量作废/召回</p>
	  * @author JZR  
	 * @throws Exception 
	  * @date 2018年5月28日
	  */
	@GetMapping("/update/butch")
	public ResultVO downButchCipher(@RequestParam("companyId")Integer companyId, 
									@RequestParam("batch")String batch,
									@RequestParam("privateKey") String privateKey,
									@RequestParam("status") String status) throws Exception {
		antifakeService.updateButchCipher(companyId,batch,privateKey,status);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 按序号批量作废/召回</p>
	  * @author JZR  
	 * @throws Exception 
	  * @date 2018年5月28日
	  */
/*	@PostMapping("/update/downcode")
	public ResultVO downCodeCipher(@RequestParam(name="companyId",required=true)Integer companyId,
								   @RequestParam(name="begain",required=true)String begain,
								   @RequestParam(name="end",required=true)String end,
								   @RequestParam(name="privateKey",required=true)String privateKey,
								   @RequestParam(name="status",required=true)String status) throws Exception {
		antifakeService.updateCodeCipher(companyId,begain,end,privateKey,status);
		return ResultVOUtil.success();
	}
	*/
	
	@PostMapping("/update/downcode")
	public ResultVO downCodeCipher(@Valid @RequestBody AntifakeForm antifakeForm, BindingResult bindingResult) throws Exception {
		
		if(bindingResult.hasErrors()) {
			log.error("【申请公司角色】请求参数有误, companyFore = {}", antifakeForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		Antifake converteAntifake = AntifakeForm2AntifakeModelConverter.converte(antifakeForm);
		
		antifakeService.updateCodeCipher(converteAntifake);
		return ResultVOUtil.success();
		
	}
	
	
	
	
	/**
	  * <p>Description: 统计生成</p>
	  * @author JZR  
	 * @throws Exception 
	  * @date 2018年5月29日
	  */
	/*@PostMapping("/list/cipher")
	public ResultVO<List<Cipher>> listCipher(@RequestParam(name="companyId",required=true)Integer companyId,@RequestParam(name="companyCode",required=false)String companyCode,@RequestParam(name="productCode",required=false)String productCode,@RequestParam(name="batch",required=false)String batch,@RequestParam(name="orderBy",required=false)String orderBy,@RequestParam(name="pageNum",required=false,defaultValue="0")Integer pageNum,@RequestParam(name="pageSize",required=false,defaultValue="15")Integer pageSize) throws Exception {
		
		List<Cipher> cipherList = antifakeService.listCipher(companyId,companyCode,productCode,batch,orderBy,pageNum,pageSize);
		return ResultVOUtil.success(cipherList);
	}
	*/
	
	@PostMapping("/list/cipher")
	public ResultVO<List<Cipher>> listCipher(@Valid @RequestBody AntifakeForm antifakeForm,@RequestParam(name="orderBy",required=false)String orderBy,@RequestParam(name="pageNum",required=false,defaultValue="0")Integer pageNum,@RequestParam(name="pageSize",required=false,defaultValue="15")Integer pageSize,BindingResult bindingResult) throws Exception {
		
		
		if(bindingResult.hasErrors()) {
			log.error("list of cipher error, AntifakeFore = {}", antifakeForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		
		Antifake converteAntifake = AntifakeForm2AntifakeModelConverter.converte(antifakeForm);
		
		
		List<Cipher> cipherList = antifakeService.listCipher(converteAntifake,orderBy,pageNum,pageSize);
		return ResultVOUtil.success(cipherList);
	}
	
	
	
	
	/**
	  * <p>Description: 根据公司查询明文</p>
	  * @author JZR  
	  * @date 2018年5月30日
	  */
	@GetMapping("/list/expre/{companyId}")
	public ResultVO<List<ExpreVO>> listExpre(@PathVariable(name="companyId",required=true)Integer companyId,@RequestParam(name="pageNum",required=false,defaultValue="0")Integer pageNum,@RequestParam(name="pageSize",required=false,defaultValue="15")Integer pageSize){
		
		List<ExpreVO> expreList = antifakeService.listExpre(companyId,pageNum ,pageSize);
		
		return ResultVOUtil.success(expreList);
	}
	
}
