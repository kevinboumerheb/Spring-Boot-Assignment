package com.springbootassignment.controller;

import com.springbootassignment.model.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.springbootassignment.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserModel> retrieveAllUsers() {
        return userService.retrieveAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public UserModel retrieveUser(@PathVariable int id) {
        return userService.retrieveUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        return userService.deleteUserById(id);
    }
}
