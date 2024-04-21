package com.springbootassignment.controller;


import com.springbootassignment.model.CustomerModel;
import com.springbootassignment.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CustomerModel customer) {
        return loginService.registerUser(customer);
    }

    @RequestMapping("/user")
    public CustomerModel getUserDetailsAfterLogin(Authentication authentication) {
        return loginService.getUserDetailsAfterLogin(authentication);
    }
}
