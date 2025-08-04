package com.example.gyeol.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String role;
}
