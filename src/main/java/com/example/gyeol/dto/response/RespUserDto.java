package com.example.gyeol.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespUserDto {
    private Long userId;
    private String username;
    private String role;

}
