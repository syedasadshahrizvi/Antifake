package com.antifake.converter;

import com.antifake.VO.AddressVO;
import com.antifake.model.Address;

public class Address2AddressVO {
	public static AddressVO converter(Address address) {
		AddressVO addressVO = new AddressVO();
		addressVO.setAddress(address.getAddress());
		addressVO.setCity(address.getCity());
		addressVO.setCommunity(address.getCommunity());
		addressVO.setCounty(address.getCounty());
		addressVO.setIsdefault(address.getIsdefault());
		addressVO.setName(address.getName());
		addressVO.setPostCode(address.getPostCode());
		addressVO.setProvince(address.getProvince());
		addressVO.setTelphone(address.getTelphone());
		addressVO.setAddressId(address.getAddressId());
		addressVO.setUserId(address.getUserId());
		
		return addressVO;
	}
}
