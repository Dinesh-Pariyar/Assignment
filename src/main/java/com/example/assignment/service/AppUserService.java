package com.example.assignment.service;

import com.example.assignment.dto.AppUserDto;
import com.example.assignment.model.AppUser;

import java.util.List;

public interface AppUserService {

    public List<AppUserDto> getAllUser();

    public AppUserDto getUserByUsername(String username);

    public  String saveUser(AppUser user);


}
