package com.example.assignment.dto;

import com.example.assignment.model.AppUser;

import java.util.List;
import java.util.stream.Collectors;

public class RequestResponseMapper {


    public static AppUserDto convertAppUserToDto(AppUser appUser) {

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setFullName(appUser.getFullName());
        appUserDto.setUserName(appUser.getUsername());

        return appUserDto;
    }


    public static List<AppUserDto> fetchAllAppUserDtos(List<AppUser> appUsers) {

        return appUsers.stream()
                .map(RequestResponseMapper::convertAppUserToDto).collect(Collectors.toList());
    }


}
