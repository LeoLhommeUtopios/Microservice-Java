package org.example.demo_resilience4j.controller;

import org.example.demo_resilience4j.model.User;
import org.example.demo_resilience4j.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/circuit-breaker/{id}")
    public User getUserWithCircuitBreaker (@PathVariable Long id){
        return userService.getUserWithCicuitBreaker(id);
    }

    @GetMapping("/retry/{id}")
    public User getUserWithretry (@PathVariable Long id){
        return userService.getUserWithRetry(id);
    }
    @GetMapping("/combine/{id}")
    public User getUserCombined (@PathVariable Long id){
        return userService.getUserWithCircuitBreakerAndRetry(id);
    }
    @GetMapping("/timeout/{id}")
    public User getUserwithTimeOut (@PathVariable Long id) throws ExecutionException, InterruptedException {
        return userService.getuserWithTimeLimiter(id).get();
    }

    @GetMapping()
    public String reset (){
        userService.resetClient();
        return "compteur reset";
    }


}
