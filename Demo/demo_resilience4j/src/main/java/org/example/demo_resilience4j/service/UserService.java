package org.example.demo_resilience4j.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.example.demo_resilience4j.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserServiceClient userServiceClient;

    public UserService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @CircuitBreaker(name = "userservice",fallbackMethod = "getFallBackUser")
    public User getUserWithCicuitBreaker (Long id){
        System.out.println("appel avec cicuit breaker");
        return userServiceClient.getUserById(id);
    }

    @Retry(name = "userservice",fallbackMethod = "getFallBackUser")
    public User getUserWithRetry (Long id){
        System.out.println("appel avec Retry");
        return userServiceClient.getUserById(id);
    }

    @Retry(name = "userservice",fallbackMethod = "getFallBackUser")
    @CircuitBreaker(name = "userservice",fallbackMethod = "getFallBackUser")
    public User getUserWithCircuitBreakerAndRetry(Long id){
        System.out.println("appel avec Circuitbreaker et retry");
        return userServiceClient.getUserById(id);
    }

    @TimeLimiter(name = "userservice",fallbackMethod = "getFallBackUserAsync")
    public CompletableFuture<User> getuserWithTimeLimiter (Long id){
        System.out.println("appel avec time limiter");
        return CompletableFuture.supplyAsync(() -> userServiceClient.getSlowUser(id));
    }

    @TimeLimiter(name = "userservice",fallbackMethod = "getFallBackUserAsync")
    @CircuitBreaker(name = "userservice",fallbackMethod = "getFallBackUser")
    @Retry(name = "userservice",fallbackMethod = "getFallBackUser")
    public CompletableFuture<User> getuserWithFullProtection (Long id){
        System.out.println("appel avec time limiter");
        return CompletableFuture.supplyAsync(() -> userServiceClient.getSlowUser(id));
    }

    private User getFallBackUser (Long id,Throwable e){
        System.out.println("Fallback activé " +e.getMessage());
        return new User(id,"defaultUser","default@email.com");
    }

    private CompletableFuture<User> getFallBackUserAsync (Long id,Throwable e){
        System.out.println("Fallback activé " +e.getMessage());
        return CompletableFuture.completedFuture(
                new User(id,"defaultUser","default@email.com")
        );
    }

    public void resetClient (){
        userServiceClient.resetCount();
    }
}
