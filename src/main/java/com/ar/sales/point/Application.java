package com.ar.sales.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		// Force JVM timezone to UTC early to avoid passing unsupported timezone strings to Postgres
		System.setProperty("user.timezone", "UTC");
		SpringApplication.run(Application.class, args);
	}

}
