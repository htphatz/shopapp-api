package com.example.shopapp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ShopappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappApplication.class, args);
	}

	@Bean
	public Cloudinary getCloudinary(){
		Map<String, String> config = new HashMap();
		config.put("cloud_name", "drgidfvnd");
		config.put("api_key", "195446941243745");
		config.put("api_secret", "4RQj5g79A55YCQeybvBxXBTTIq8");
		config.put("secure", "true");
		return new Cloudinary(config);
	}

}
