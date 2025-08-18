package com.example.gyeol.controller;

import com.example.gyeol.dto.response.RespUserDto;
import com.example.gyeol.entity.Users;
import com.example.gyeol.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;  // ⬅️ @PreAuthorize 사용
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/api/demo/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> userHello() {
        return ResponseEntity.ok("USER or ADMIN OK");
    }

    @GetMapping("/api/demo/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminHello() {
        return ResponseEntity.ok("ADMIN ONLY OK");
    }


    @GetMapping("/api/auth/me")
    public ResponseEntity<RespUserDto> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return ResponseEntity.status(401).build();

        String username = auth.getName(); // 현재는 username 기반
        Users u = userService.getByUsername(username);

        return ResponseEntity.ok(
                RespUserDto.builder()
                        .userId(u.getUserId())
                        .username(u.getUsername())
                        .role(u.getRole())
                        .build()
        );
    }

    // 로그아웃
    // - 서버 세션이 없으므로, 로그아웃의 본질은 클라이언트가 토큰을 버리는 것"
    // - 이 엔드포인트는 프론트가 흐름을 통일하려고 호출하는 형식적 API (서버 상태 변경 없음)
    // - 응답을 200으로 내려주고, 프론트는 로컬스토리지/쿠키에서 accessToken 삭제하면 끝.
    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout() {
        // 여기서는 서버에서 할 일이 없음(Stateless). 프론트에서 토큰을 폐기하도록 안내.
        return ResponseEntity.ok().body("Logged out (client must discard the token)");
    }
}
