package com.example.gyeol.service;


import com.example.gyeol.dto.request.UserDto;
import com.example.gyeol.entity.Users;
import com.example.gyeol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Long register(UserDto userDto){
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }
        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role("USER")
                .build();
        return userRepository.save(user).getUserId();
    }
}
