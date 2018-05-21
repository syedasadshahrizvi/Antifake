package com.antifake.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antifake.model.PubKey;

public interface PubKeyRepository extends JpaRepository<PubKey, Integer>{

}
