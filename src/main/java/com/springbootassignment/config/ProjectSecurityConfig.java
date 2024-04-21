package com.springbootassignment.config;

import com.springbootassignment.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;


@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
//        requestHandler.setCsrfRequestAttributeName("_csrf");

//        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers( "/register")
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
//                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
//                .authorizeHttpRequests((requests)->requests
//                        .requestMatchers("hello/kevin").authenticated()
//                        .requestMatchers("register").permitAll())
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();

        http.csrf((csrf) -> csrf.disable())
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/users","/albums", "/photos").hasRole("ADMIN")
                        .requestMatchers("/users", "/user/{id}", "/albums", "/user/{id}/albums",
                                "/user/{id}/album/{albumId}", "/photos", "/user/{id}/photos",
                                "/user/{userId}/album/{albumId}/photos", "/user/{userId}/album/{albumId}/photo/{photoId}").authenticated()
                        .requestMatchers("/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
