package com.example.shopapp.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "ShopApp APIs", version = "1.0", description = "ShopApp APIs"),
        servers = {
                @Server(url = "http://localhost:8080"),
                @Server(url = "https://shopapp.vutiendat3601.io.vn")
        }
)
public class OpenApiConfig {
}
