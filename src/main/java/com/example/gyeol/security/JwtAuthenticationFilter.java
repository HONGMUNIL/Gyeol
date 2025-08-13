package com.example.gyeol.security;

import com.example.gyeol.entity.Users;
import com.example.gyeol.repository.UserRepository;
import com.example.gyeol.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 최소 구성:
 * - Authorization: Bearer <token> 추출
 * - JwtUtil.parseToken으로 검증/파싱
 * - jti(=userId)로 DB 조회 → SecurityContext에 인증 심기
 */
@Component
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String token = resolveBearer(request);

        if (token != null) {
            try {
                Claims claims = jwtUtil.parseToken(token); // 서명/만료 검증 + 클레임 파싱
                String jti = claims.getId();               // 토큰 만들 때 setId(userId) 한 값
                if (StringUtils.hasText(jti)) {
                    Long userId = Long.parseLong(jti);
                    Optional<Users> opt = userRepository.findById(userId);
                    if (opt.isPresent()) {
                        Users user = opt.get();
                        String role = (user.getRole() == null) ? "USER" : user.getRole();
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 토큰 위조/만료 등 문제면 401로 응답 후 종료
                SecurityContextHolder.clearContext();
                ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("잘못된 형식입니다. JWT가 만료되었거나 위조되었습니다");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private String resolveBearer(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
