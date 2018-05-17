package com.antifake.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antifake.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

	Address findByUserIdAndIsdefault(String userId, Integer i);
	
}
