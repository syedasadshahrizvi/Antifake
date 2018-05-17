package com.antifake;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@MapperScan("com.antifake.mapper")
@EnableJpaAuditing
public class AntifakeApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(AntifakeApplication.class, args);
	}
}
