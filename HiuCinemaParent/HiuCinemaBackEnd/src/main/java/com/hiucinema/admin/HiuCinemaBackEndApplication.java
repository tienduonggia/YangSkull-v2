package com.hiucinema.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.hiucinema.common.entity" , "com.hiucinema.admin.user"})
public class HiuCinemaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiuCinemaBackEndApplication.class, args);
	}

}
