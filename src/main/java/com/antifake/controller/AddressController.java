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

import com.antifake.VO.AddressVO;
import com.antifake.VO.ResultVO;
import com.antifake.converter.AddressForm2AddressModel;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.AddressForm;
import com.antifake.model.Address;
import com.antifake.service.AddressService;
import com.antifake.utils.ResultVOUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	
	/**
	  * <p>Description: 添加地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@PostMapping("/add")
	public ResultVO	add(@Valid AddressForm addressForm ,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			log.error("【地址添加】参数不正确，addressForm={}", addressForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
		}
		
		Address address = AddressForm2AddressModel.converte(addressForm);
		addressService.addAddress(address);
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 修改地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@PostMapping("/edit")
	public ResultVO edit(@Valid AddressForm addressForm ,BindingResult bindingResult) {
		if(bindingResult.hasErrors() || addressForm.getAddressId()==null) {
			log.error("【编辑地址】参数不正确，addressForm={}", addressForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(), addressForm.getAddressId()==null?ResultEnum.ID_ERROR.getMessage():bindingResult.getFieldError().getDefaultMessage());
		}
		Address address = AddressForm2AddressModel.converte(addressForm);
		addressService.updateAddress(address);
		
		return ResultVOUtil.success();
	}
	
	/**
	  * <p>Description: 设置默认地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@GetMapping("/def/{addressId}/{userId}")
	public ResultVO setDefault(@PathVariable("addressId")Integer addressId,@PathVariable("userId")String userId) {
		addressService.setDefault(addressId,userId);
		return ResultVOUtil.success();
	}

	/**
	  * <p>Description: 根据用户id查询地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@GetMapping("/list/{userId}")
	public ResultVO<List<AddressVO>> selectList(@PathVariable("userId")String userId){
		
		List<AddressVO> listAddressVO = addressService.selectListByUid(userId);
		
		return ResultVOUtil.success(listAddressVO);
	}
	
	/**
	  * <p>Description: 查询地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@GetMapping("/get/{addressId}")
	public ResultVO<AddressVO> select(@PathVariable("addressId")Integer addressId){
		
		AddressVO addressVO = addressService.selectListByAddressId(addressId);
		
		return ResultVOUtil.success(addressVO);
	}
	
	/**
	  * <p>Description: 删除地址</p>
	  * @author JZR  
	  * @date 2018年5月16日
	  */
	@GetMapping("/delete/{addressId}")
	public ResultVO delete(@PathVariable("addressId")Integer addressId) {
		addressService.deleteByaddressId(addressId);
		return ResultVOUtil.success();
	}
	
}
