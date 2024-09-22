package com.raqamiEcommerce.EcommerceShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootApplication
public class EcommerceShopApplication {
	public String PORT=System.getenv("PORT");

	public static void main(String[] args) {
		SpringApplication.run(EcommerceShopApplication.class, args);
	}

}
