package org.example.demo_security.service;

import org.example.demo_security.dto.RegisterRequestDto;
import org.example.demo_security.model.UserApp;
import org.example.demo_security.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAppService {

    private final UserRepository repository;

    public UserAppService (UserRepository repository){
        this.repository =repository;
    }

    public UserApp registerUser (RegisterRequestDto registerRequestDto){
        UserApp userApp= new UserApp(registerRequestDto.getEmail(),registerRequestDto.getPassword(),registerRequestDto.getFirstname(),registerRequestDto.getLastname());

        return repository.save(userApp);
    }
}
