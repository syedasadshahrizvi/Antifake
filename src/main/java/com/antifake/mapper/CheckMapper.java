package com.antifake.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.antifake.model.Check;

public interface CheckMapper {

	void insertList(@Param("listCheck")List<Check> listCheck);

	Check queryCheck(@Param("check")Check check);

	Integer updateCount(@Param("check")Check check);

}
