package com.example.gyeol.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RespTokenDto {

//    @Schema(description = "액세스 토큰(JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

//    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;

//    @Schema(description = "만료 시각(Epoch ms)", example = "1736563200000")
    private long expiresAt;

    // 필요해지면 주석 해제해서 사용
    // @Schema(description = "리프레시 토큰", example = "def456...")
    // private String refreshToken;
    // @Schema(description = "리프레시 만료 시각(Epoch ms)", example = "1739168800000")
    // private Long refreshExpiresAt;
}
