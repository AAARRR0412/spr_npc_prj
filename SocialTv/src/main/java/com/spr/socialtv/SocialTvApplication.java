package com.spr.socialtv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class SocialTvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialTvApplication.class, args);
	}

}
