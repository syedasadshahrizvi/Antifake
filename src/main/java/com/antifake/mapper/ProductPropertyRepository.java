package com.antifake.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antifake.model.ProductProperty;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Integer> {

}
