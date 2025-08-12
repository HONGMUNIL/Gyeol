package com.example.gyeol.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqLoginDto {
    @NotBlank private String username;
    @NotBlank private String password;
}
