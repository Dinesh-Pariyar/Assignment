package com.example.assignment.service.serviceImpl;

import com.example.assignment.dto.AppUserDto;
import com.example.assignment.dto.RequestResponseMapper;
import com.example.assignment.model.AppUser;
import com.example.assignment.repo.AppUserRepo;
import com.example.assignment.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;


    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<AppUserDto> getAllUser() {
        return RequestResponseMapper.fetchAllAppUserDtos(appUserRepo.findAll());
    }

    @Override
    public AppUserDto getUserByUsername(String username) {
        return RequestResponseMapper
                .convertAppUserToDto(appUserRepo.findAppUserByUsername(username).orElseThrow(() -> new RuntimeException("User with username not found")));
    }

    @Override
    public String saveUser(AppUser appUser) {
        appUser.setPassword(encoder.encode(appUser.getPassword()));
        appUserRepo.save(appUser);
        return "User saved Successfully";
    }

    @Override
    public List<String> seeFacility() {
        return Arrays.asList("Free Food","Competitive Salary","Dashain Bonus");
    }


}
