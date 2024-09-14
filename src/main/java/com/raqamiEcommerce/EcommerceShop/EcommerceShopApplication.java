package com.raqamiEcommerce.EcommerceShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceShopApplication {
	public String PORT=System.getenv("PORT");

	public static void main(String[] args) {
		SpringApplication.run(EcommerceShopApplication.class, args);
	}

}
