package com.atalibdev.controller;

import com.atalibdev.entities.User;
import com.atalibdev.request.ChangePasswordRequest;
import com.atalibdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class ApplicationController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> message() {
        return ResponseEntity.ok("Hello From Secured page");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUsers() {
        return new ResponseEntity<>("Getting users", HttpStatus.OK);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Principal principal) {
        userService.changePassword(request, principal);
        return ResponseEntity.ok().build();
    }
}
