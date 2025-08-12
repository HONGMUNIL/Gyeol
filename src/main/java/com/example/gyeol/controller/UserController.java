package com.example.gyeol.controller;


import com.example.gyeol.dto.request.ReqLoginDto;
import com.example.gyeol.dto.request.ReqUserDto;
import com.example.gyeol.dto.response.RespLoginDto;
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
    public Long register(@RequestBody @Valid ReqUserDto reqUserDto){
        return userService.register(reqUserDto);
    }

    @PostMapping("/login")
    public RespLoginDto Login(@RequestBody @Valid ReqLoginDto reqLoginDto){
        var users = userService.login(reqLoginDto.getUsername(), reqLoginDto.getPassword());
        return RespLoginDto.builder()
                .id(users.getUserId())
                .username(users.getUsername())
                .role(users.getRole())
                .build();
    }

}
