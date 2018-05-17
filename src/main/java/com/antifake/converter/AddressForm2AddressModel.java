package com.antifake.converter;

import com.antifake.form.AddressForm;
import com.antifake.model.Address;

public class AddressForm2AddressModel {
	public static Address converte(AddressForm addressForm) {
		Address address = new Address();
		address.setAddress(addressForm.getAddress());
		address.setCity(addressForm.getCity());
		address.setCommunity(addressForm.getCommunity());
		address.setCounty(addressForm.getCounty());
		address.setIsdefault(addressForm.getIsdefault());
		address.setName(addressForm.getName());
		address.setPostCode(addressForm.getPostCode());
		address.setProvince(addressForm.getProvince());
		address.setTelphone(addressForm.getTelphone());
		address.setUserId(addressForm.getUserId());
		address.setAddressId(addressForm.getAddressId());
		
		return address;
	}
}
