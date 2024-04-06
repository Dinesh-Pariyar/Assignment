package com.example.assignment.controller;

import com.example.assignment.model.AppUser;
import com.example.assignment.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/home")
    public ResponseEntity<?> welcomeUser() {
        return ResponseEntity.ok().body("Welcome To First Round Assignment Application");
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok().body(appUserService.getAllUser());
    }

    @GetMapping("/user/facility")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> getUserById() {
        return ResponseEntity.ok().body(appUserService.seeFacility());
    }


    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok().body(appUserService.saveUser(appUser));
    }
}
