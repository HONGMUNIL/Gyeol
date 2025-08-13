package com.example.gyeol.controller;


import com.example.gyeol.dto.request.ReqLoginDto;
import com.example.gyeol.dto.request.ReqUserDto;
import com.example.gyeol.dto.response.RespLoginDto;
import com.example.gyeol.dto.response.RespTokenDto;
import com.example.gyeol.dto.response.RespUserDto;
import com.example.gyeol.entity.Users;
import com.example.gyeol.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.gyeol.security.jwt.JwtUtil;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Long register(@RequestBody @Valid ReqUserDto reqUserDto){
        return userService.register(reqUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<RespTokenDto> login(@RequestBody @Valid ReqLoginDto dto) {
        // 1) 자격 검증 (Users 반환)
        Users user = userService.login(dto.getUsername(), dto.getPassword());

        // 2) 토큰 발급 (sub=username, jti=userId)
        String token = jwtUtil.generateToken(user.getUsername(), String.valueOf(user.getUserId()));

        // 3) 만료 시각 추출
        Claims claims = jwtUtil.parseToken(token);
        long expiresAt = claims.getExpiration().getTime();

        // 4) 토큰 DTO만 반환 (로그인 응답은 인증 데이터만!)
        RespTokenDto resp = RespTokenDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresAt(expiresAt)
                .build();

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<RespUserDto> me(@RequestHeader("Authorization") String authorization) {
        // Authorization: Bearer <token>
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authorization.substring(7);

        // 1) 토큰 검증/파싱 → jti(=userId), exp 등
        Claims claims = jwtUtil.parseToken(token);
        Long userId = Long.parseLong(claims.getId());

        // 2) PK로 사용자 조회
        Users user = userService.getById(userId);

        // 3) 응답 DTO 구성
        return ResponseEntity.ok(
                RespUserDto.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .build()
        );
    }

}
