package com.atalibdev.controller;

import com.atalibdev.entities.User;
import com.atalibdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> message() {
        return ResponseEntity.ok("Hello From Secured page");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.fetchUsers();
        return ResponseEntity.ok(userList);
    }
}
