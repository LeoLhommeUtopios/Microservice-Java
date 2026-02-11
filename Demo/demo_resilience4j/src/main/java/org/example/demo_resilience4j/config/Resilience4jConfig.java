package org.example.demo_resilience4j.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig(){
        return CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(3)
                .recordExceptions(Throwable.class)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig config){
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker userServiceCircuitBreaker (CircuitBreakerRegistry registry){
        CircuitBreaker circuitBreaker = registry.circuitBreaker("userservice");

        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        System.out.println("circuit breaker etat:"+ event.getStateTransition()))
                .onFailureRateExceeded(event ->
                        System.out.println("taux d'echec depassé"+event.getFailureRate()))
                .onCallNotPermitted(event ->
                        System.out.println("appel bloqué par cicuit breacker"));

        return circuitBreaker;
    }

    @Bean
    public RetryConfig retryConfig(){
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(Throwable.class)
                .ignoreExceptions(IllegalArgumentException.class)
                .build();
    }

    @Bean
    public RetryRegistry registryRegistry (RetryConfig config){
        return RetryRegistry.of(config);
    }

    @Bean
    public Retry userServiceRetry (RetryRegistry registry){
        Retry retry = registry.retry("userservice");
        retry.getEventPublisher()
                .onRetry(event ->
                        System.out.println("retry tentatentive "+event.getNumberOfRetryAttempts()))
                .onSuccess(event ->
                        System.out.println("retry reussi apres "+event.getNumberOfRetryAttempts() +" tentative"))
                .onError(event ->
                        System.out.println("retry echoué apres "+event.getNumberOfRetryAttempts() +" tentative"));
        return retry;
    }

    @Bean
    public TimeLimiterConfig timeLimiterConfig(){
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .cancelRunningFuture(true)
                .build();
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(TimeLimiterConfig config){
        return TimeLimiterRegistry.of(config);
    }

    @Bean
    public TimeLimiter userServiceTimeLimiter(TimeLimiterRegistry registry){
        return registry.timeLimiter("userservice");
    }
}
