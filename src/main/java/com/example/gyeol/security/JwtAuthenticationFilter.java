package com.example.gyeol.security;

import com.example.gyeol.entity.Users;
import com.example.gyeol.repository.UserRepository;
import com.example.gyeol.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JWT 인증 필터:
 * - 공개 경로(/api/auth/** 등)는 스킵
 * - 그 외 경로에서 Bearer 토큰을 파싱/검증 후 인증 컨텍스트 채움
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return pathMatcher.match("/api/auth/**", uri)   // 로그인/회원가입 등
                || pathMatcher.match("/health", uri)
                || pathMatcher.match("/actuator/**", uri);  // 필요 시 추가
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {


        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response); // 토큰 없으면 통과
            return;
        }

        String token = header.substring(7);

        try {
            // 토큰 검증 + 파싱
            Claims claims = jwtUtil.parseToken(token);

            // jti(사용자 PK)로 DB에서 사용자 로드
            String jti = claims.getId();
            if (!StringUtils.hasText(jti)) {
                chain.doFilter(request, response);
                return;
            }

            // JPA PK 타입에 맞게 변환 (Long 가정 — Integer면 Integer.parseInt 사용)
            Long userId = Long.parseLong(jti);
            Optional<Users> opt = userRepository.findById(userId);
            if (opt.isEmpty()) {
                chain.doFilter(request, response);
                return;
            }
            Users user = opt.get();

            //  권한 매핑: DB의 role 값(USER/ADMIN 등)을 "ROLE_*"로 변환
            String role = (user.getRole() == null) ? "USER" : user.getRole();
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            //  인증 객체 생성 (principal로 username만 넣어도 되고, User 전체를 넣어도 됨)
            var authentication =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

            // SecurityContext에 저장 → 이후 @PreAuthorize, hasRole 동작
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            // 토큰 위조/만료/형식오류 → 401
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired JWT");
            return;
        }

        chain.doFilter(request, response);
    }
}
