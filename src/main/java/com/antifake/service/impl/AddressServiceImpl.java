package com.antifake.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.antifake.VO.AddressVO;
import com.antifake.converter.Address2AddressVO;
import com.antifake.mapper.AddressRepository;
import com.antifake.model.Address;
import com.antifake.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{
	
	public static final Integer TRUE_STATUS = 1;
	public static final Integer FALSE_STATUS = 0;
	public static final Integer IS_DEFAULT = 1;
	public static final Integer NO_DEFAULT = 0;
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public AddressVO addAddress(Address address) {
		address.setStatus(TRUE_STATUS);
		Address save = addressRepository.save(address);
		AddressVO converter = Address2AddressVO.converter(save);
		return converter;
	}

	@Override
	public AddressVO updateAddress(Address address) {
		Address addressExample = new Address();
		addressExample.setAddressId(address.getAddressId());
		Example<Address> example = Example.of(addressExample);
		Address one = addressRepository.findOne(example).get();
		one.setAddress(address.getAddress());
		one.setCity(address.getCity());
		one.setCommunity(address.getCommunity());
		one.setCounty(address.getCounty());
		one.setName(address.getName());
		one.setPostCode(address.getPostCode());
		one.setProvince(address.getProvince());
		one.setTelphone(address.getTelphone());
		Address save = addressRepository.save(one);
		AddressVO converter = Address2AddressVO.converter(save);
		return converter;
	}

	@Override
	public AddressVO setDefault(Integer addressId,String userId) {
		
		Address address = addressRepository.findByUserIdAndIsdefault(userId,IS_DEFAULT);
		if(address!=null) {
			address.setIsdefault(NO_DEFAULT);
			addressRepository.save(address);
		}
		Address address2 = new Address();
		address2.setAddressId(addressId);
		Example<Address> example = Example.of(address2);
		Address one = addressRepository.findOne(example).get();
		one.setIsdefault(IS_DEFAULT);
		Address save = addressRepository.save(one);
		AddressVO converter = Address2AddressVO.converter(save);
		return converter;
	}

	@Override
	public List<AddressVO> selectListByUid(String userId) {
		Address address = new Address();
		address.setUserId(userId);
		address.setStatus(TRUE_STATUS);
		Example<Address> example = Example.of(address);
		List<Address> findAll = addressRepository.findAll(example);
		List<AddressVO> addressVOList = new ArrayList<AddressVO>();
		for (Address address2 : findAll) {
			AddressVO converter = Address2AddressVO.converter(address2);
			addressVOList.add(converter);
		}
		return addressVOList;
	}

	@Override
	public AddressVO selectListByAddressId(Integer addressId) {
		Address address = new Address();
		address.setAddressId(addressId);
		Example<Address> example = Example.of(address);
		Address one = addressRepository.findOne(example).get();
		AddressVO converter = Address2AddressVO.converter(one);
		return converter;
	}

	@Override
	public AddressVO deleteByaddressId(Integer addressId) {
		Address address = addressRepository.findById(addressId).get();
		address.setStatus(FALSE_STATUS);
		Address save = addressRepository.save(address);
		AddressVO converter = Address2AddressVO.converter(save);
		return converter;
	}

}
