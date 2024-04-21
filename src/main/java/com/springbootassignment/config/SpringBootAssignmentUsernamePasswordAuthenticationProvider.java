package com.springbootassignment.config;

import com.springbootassignment.model.AuthorityModel;
import com.springbootassignment.model.CustomerModel;
import com.springbootassignment.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringBootAssignmentUsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public SpringBootAssignmentUsernamePasswordAuthenticationProvider(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<CustomerModel> customer = customerRepository.findByEmail(username);
        if(customer.size() > 0){
            if(passwordEncoder.matches(password, customer.get(0).getPwd())){
                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(customer.get(0).getAuthorities()));
            }else{
                throw new BadCredentialsException("Invalid password");
            }
        }else {
            throw new BadCredentialsException("No user registered with this email");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<AuthorityModel> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (AuthorityModel authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}