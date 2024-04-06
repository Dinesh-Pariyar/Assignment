package com.example.assignment.auth;

import com.example.assignment.config.AppUserInfoDetails;
import com.example.assignment.model.AppUser;
import com.example.assignment.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepo.findAppUserByUsername(username);
        return appUser.map(AppUserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
