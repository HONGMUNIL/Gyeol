package com.example.gyeol.service;


import com.example.gyeol.dto.request.ReqUserDto;
import com.example.gyeol.entity.Users;
import com.example.gyeol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Long register(ReqUserDto reqUserDto){
        if (userRepository.existsByUsername(reqUserDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }
        Users user = Users.builder()
                .username(reqUserDto.getUsername())
                .password(passwordEncoder.encode(reqUserDto.getPassword()))
                .role("USER")
                .build();
        return userRepository.save(user).getUserId();
    }

    public Users login(String username, String password){
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        if(!passwordEncoder.matches(password, users.getPassword())){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        return users; // 이후 JWT 붙일 때 여기서 토큰 발급로직 추가
    }
}
