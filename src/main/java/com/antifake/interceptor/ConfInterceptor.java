package com.antifake.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.antifake.service.RoleService;

@Configuration
public class ConfInterceptor extends WebMvcConfigurationSupport{
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private RoleService roleSerice;
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor(redisTemplate,roleSerice)).addPathPatterns("/**");
	}
}
