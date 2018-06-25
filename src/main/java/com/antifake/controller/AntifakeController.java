package com.antifake.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ExpreVO;
import com.antifake.VO.ResultVO;
import com.antifake.form.AntifakeFrom;
import com.antifake.model.Cipher;
import com.antifake.service.AntifakeService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/antifake")
@Slf4j
public class AntifakeController {

	@Autowired
	private AntifakeService antifakeService;

	/**
	 * <p>
	 * Description: 生成防伪码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年5月7日
	 */
	@PostMapping("/create")
	public ResultVO<List<String>> createCipher(@RequestBody AntifakeFrom antifakeFrom) {
		List<String> list = antifakeService.encrypt(antifakeFrom.getPrivateKey(), antifakeFrom.getCompanyId(), antifakeFrom.getCompanyCode(), antifakeFrom.getProductCode(), antifakeFrom.getTemplate(), antifakeFrom.getCount());
		return ResultVOUtil.success(list);
	}
	
	@PostMapping("/create2")
	public ResultVO<List<String>> createCipher2(@RequestBody AntifakeFrom antifakeFrom) {
		List<String> list = antifakeService.encrypt2(antifakeFrom.getPrivateKey(), antifakeFrom.getCompanyId(), antifakeFrom.getCompanyCode(), antifakeFrom.getProductCode(), antifakeFrom.getTemplate(), antifakeFrom.getCount());
		return ResultVOUtil.success(list);
	}

	/**
	 * <p>
	 * Description: 校验防伪码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年5月7日
	 */
	@GetMapping("/checked/{codeString}/{type}")
	public ResultVO<Map<String, Object>> checkCipher(@PathVariable("codeString") String codeString,
			@PathVariable(name = "type") String type) throws Exception {
		Map<String, Object> resultMap = antifakeService.checkCode(codeString, type);
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
	@PostMapping("/update")
	public ResultVO downCipher(@RequestParam("cipherId") Integer cipherId, 
							   @RequestParam("companyId") Integer companyId,
							   @RequestParam("privateKey") String privateKey,
							   @RequestParam("status") String status) throws Exception {
		antifakeService.updateCipher(cipherId, companyId, privateKey,status);
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
	@PostMapping("/update/downcode")
	public ResultVO downCodeCipher(@RequestParam(name="companyId",required=true)Integer companyId,
								   @RequestParam(name="begain",required=true)String begain,
								   @RequestParam(name="end",required=true)String end,
								   @RequestParam(name="privateKey",required=true)String privateKey,
								   @RequestParam(name="status",required=true)String status) throws Exception {
		antifakeService.updateCodeCipher(companyId,begain,end,privateKey,status);
		return ResultVOUtil.success();
	}
	
	
	/**
	  * <p>Description: 统计生成</p>
	  * @author JZR  
	 * @throws Exception 
	  * @date 2018年5月29日
	  */
	@PostMapping("/list/cipher")
	public ResultVO<List<Cipher>> listCipher(@RequestParam(name="companyId",required=true)Integer companyId,@RequestParam(name="companyCode",required=false)String companyCode,@RequestParam(name="productCode",required=false)String productCode,@RequestParam(name="batch",required=false)String batch,@RequestParam(name="orderBy",required=false)String orderBy,@RequestParam(name="pageNum",required=false,defaultValue="0")Integer pageNum,@RequestParam(name="pageSize",required=false,defaultValue="15")Integer pageSize) throws Exception {
		
		List<Cipher> cipherList = antifakeService.listCipher(companyId,companyCode,productCode,batch,orderBy,pageNum,pageSize);
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
