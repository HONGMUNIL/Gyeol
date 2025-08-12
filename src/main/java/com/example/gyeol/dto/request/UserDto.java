package com.example.gyeol.dto.request;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 50)
    private String username;

    @jakarta.validation.constraints.NotBlank
    private String password;
    private String role;
}
