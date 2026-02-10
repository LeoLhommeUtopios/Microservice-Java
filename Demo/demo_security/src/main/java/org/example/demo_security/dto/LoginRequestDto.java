package org.example.demo_security.dto;

public record LoginRequestDto(
        String email,
        String password
) {
}
