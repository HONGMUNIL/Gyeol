package com.example.gyeol.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespLoginDto {
    private Long id;
    private String username;
    private String role;
}
