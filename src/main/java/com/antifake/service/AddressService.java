package com.antifake.service;

import java.util.List;

import com.antifake.VO.AddressVO;
import com.antifake.model.Address;

public interface AddressService {

	AddressVO addAddress(Address address);

	AddressVO updateAddress(Address address);

	AddressVO setDefault(Integer addressId,String userId);

	List<AddressVO> selectListByUid(String userId);

	AddressVO selectListByAddressId(Integer addressId);

	AddressVO deleteByaddressId(Integer addressId);

}
