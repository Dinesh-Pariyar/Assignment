package com.example.assignment.tokenService;

import com.example.assignment.model.AppUser;
import com.example.assignment.model.RefreshToken;
import com.example.assignment.repo.AppUserRepo;
import com.example.assignment.repo.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    public RefreshToken createRefreshToken(String username) {

        Optional<AppUser> appUser = appUserRepo.findAppUserByUsername(username);
        if (appUser.isPresent()) {
            Optional<RefreshToken> existingToken = refreshTokenRepo.findByAppUser(appUser.get());
            if (existingToken.isPresent()) {
                return existingToken.get();
            } else {
                RefreshToken refreshToken = RefreshToken.builder()
                        .appUser(appUser.get())
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(600000))
                        .build();

                System.out.println(refreshToken);
                return refreshTokenRepo.save(refreshToken);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);
            throw new RuntimeException("User not Active for long time please log in ");
        }
        return token;
    }


}
