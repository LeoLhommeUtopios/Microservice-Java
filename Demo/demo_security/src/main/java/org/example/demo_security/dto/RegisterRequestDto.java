package org.example.demo_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterRequestDto {
    String email;
    String lastname;
    String firstname;
    String password;
}
