package com.example.gyeol.controller;


import com.example.gyeol.dto.request.UserDto;
import com.example.gyeol.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Long register(@RequestBody @Valid UserDto userDto){
        return userService.register(userDto);
    }

}
