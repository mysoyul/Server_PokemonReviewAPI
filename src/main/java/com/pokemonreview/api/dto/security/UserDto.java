package com.pokemonreview.api.dto.security;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;

    @Builder
    public UserDto(int id, String username, String firstName, String lastName, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}