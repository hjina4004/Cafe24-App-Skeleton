package com.lmfriends.cafe24app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Cafe24AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cafe24AppApplication.class, args);
	}

}
