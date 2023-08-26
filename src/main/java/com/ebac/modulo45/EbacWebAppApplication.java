package com.ebac.modulo45;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class EbacWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbacWebAppApplication.class, args);
	}

}
