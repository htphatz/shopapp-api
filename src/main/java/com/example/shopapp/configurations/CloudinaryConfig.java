package com.example.shopapp.configurations;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
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
