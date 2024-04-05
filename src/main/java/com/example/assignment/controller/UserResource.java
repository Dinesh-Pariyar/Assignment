package com.example.assignment.controller;

import com.example.assignment.model.AppUser;
import com.example.assignment.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserResource {

    private final AppUserService appUserService;

    @GetMapping("/home")
    public ResponseEntity<?> welcomeUser() {
        return ResponseEntity.ok().body("Welcome To Modern Application");
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok().body(appUserService.getAllUser());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable String username) {
        return ResponseEntity.ok().body(appUserService.getUserByUsername(username.trim()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok().body(appUserService.saveUser(appUser));
    }
}
