package com.example.gyeol.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespLoginDto {
    private Long userId;
    private String username;
    private String role;


    private String accessToken;   // 발급된 JWT
    private String tokenType;     // 보통 "Bearer"
    private long   expiresAt;     // 만료 시각(epoch ms)

}
