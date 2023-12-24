package com.atalibdev.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    @GetMapping
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("GET: Manager controller GET method");
    }

    @PostMapping
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("POST: Manager controller register method");
    }

    @PutMapping
    public ResponseEntity<String> update() {
        return ResponseEntity.ok("PUT: Manager controller update method");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("Delete: Manager controller delete method");
    }
}
