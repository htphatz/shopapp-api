package com.example.shopapp.configurations;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/refreshToken", apiPrefix)
                            ).permitAll()

                            // Phan quyen Role
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()

                            // Phan quyen Category
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/categories**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            // Phan quyen Product
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/products**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)

                            // Phan quyen Order
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/user/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/orders**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()

                            // Phan quyen Payment
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/payment**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/payment/**", apiPrefix)).permitAll()

                            // Phan quyen OrderDetail
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order-details/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order-details/order/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/order-details**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/order-details/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/order-details/**", apiPrefix)).permitAll()

                            // Phan quyen Voucher
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/vouchers**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/vouchers/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/vouchers**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/vouchers/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/vouchers/**", apiPrefix)).hasRole(Role.ADMIN)

                            // Phan quyen Banner
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/banners**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/banners/**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/banners**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/banners/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/banners/**", apiPrefix)).hasRole(Role.ADMIN)

                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return httpSecurity.build();
    }
}
