package com.myapp.warmwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class WarmwaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarmwaveApplication.class, args);
	}

}
