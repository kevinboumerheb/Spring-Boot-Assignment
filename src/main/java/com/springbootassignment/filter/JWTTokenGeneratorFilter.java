package com.springbootassignment.filter;


import com.springbootassignment.constants.SecurityConstants;
import com.springbootassignment.model.CustomerModel;
import com.springbootassignment.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null != authentication) {
            String username = authentication.getName();

            List<CustomerModel> customer = customerRepository.findByEmail(username);

            String phoneNumber = customer.get(0).getMobileNumber();
            int id = customer.get(0).getId();

            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Spring Boot Assignment").setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("id", id)
                    .claim("phoneNumber", phoneNumber)
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))// 8 hours
                    .signWith(key).compact();
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
            System.out.println("Generated JWT token: " + jwt);

        }

        filterChain.doFilter(request, response);
    }

    //3mol new token eza ken login
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/user");
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

}