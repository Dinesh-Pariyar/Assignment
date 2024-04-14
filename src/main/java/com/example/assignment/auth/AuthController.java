package com.example.assignment.auth;


import com.example.assignment.dto.AuthRequest;
import com.example.assignment.dto.JwtResponse;
import com.example.assignment.dto.RefreshTokenRequest;
import com.example.assignment.model.RefreshToken;
import com.example.assignment.tokenService.JwtService;
import com.example.assignment.tokenService.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/auth")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
                return ResponseEntity.ok(JwtResponse.builder()
                        .accessToken(jwtService.generateToken(authRequest.getUsername()))
                        .refreshToken(refreshToken.getToken())
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user request");
        }
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            return ResponseEntity.ok(refreshTokenService.findByToken(refreshTokenRequest.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getAppUser)
                    .map(appUser -> {
                        String accessToken = jwtService.generateToken(appUser.getUsername());
                        return JwtResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshTokenRequest.getToken())
                                .build();
                    }).orElseThrow(() -> new RuntimeException("Refresh Token is not valid or has expired")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
