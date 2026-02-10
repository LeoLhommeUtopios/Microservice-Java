package org.example.demo_security.controller;

import org.example.demo_security.dto.LoginRequestDto;
import org.example.demo_security.dto.LoginResponseDto;
import org.example.demo_security.dto.RegisterRequestDto;
import org.example.demo_security.model.UserApp;
import org.example.demo_security.security.JWTGenerator;
import org.example.demo_security.service.UserAppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserAppService userAppService;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator generator;

    public AuthController(AuthenticationManager authenticationManager, UserAppService userAppService, PasswordEncoder passwordEncoder, JWTGenerator generator) {
        this.authenticationManager = authenticationManager;
        this.userAppService = userAppService;
        this.passwordEncoder = passwordEncoder;
        this.generator = generator;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.email(),requestDto.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new LoginResponseDto(generator.generateToken(authentication)));
        }catch (Exception ex){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Boolean> register (@RequestBody RegisterRequestDto registerRequestDto){
        try{
            registerRequestDto.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            UserApp userApp = userAppService.registerUser(registerRequestDto);
            return ResponseEntity.ok(true);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(false);
        }
    }
}
