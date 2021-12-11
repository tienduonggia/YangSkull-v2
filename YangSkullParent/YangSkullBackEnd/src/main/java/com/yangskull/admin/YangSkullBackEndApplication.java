package com.yangskull.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.yangskull.common.entity" , "com.yangskull.admin.user"})
public class YangSkullBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(YangSkullBackEndApplication.class, args);
	}

}
