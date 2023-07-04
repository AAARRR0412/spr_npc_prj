package com.spr.socialtv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SocialTvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialTvApplication.class, args);
	}

}
