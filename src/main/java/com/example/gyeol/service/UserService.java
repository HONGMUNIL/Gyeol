package com.example.gyeol.service;


import com.example.gyeol.dto.request.UserDto;
import com.example.gyeol.entity.User;
import com.example.gyeol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Long register(UserDto userDto){
        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role("USER")
                .build();

        return userRepository.save(user).getUserId();
    }
}
