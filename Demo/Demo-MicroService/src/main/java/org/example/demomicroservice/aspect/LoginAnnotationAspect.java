package org.example.demomicroservice.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.demomicroservice.utils.RestClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class LoginAnnotationAspect {

    @Pointcut("@annotation(org.example.demomicroservice.annotation.Login)")
    public void loginPointCut(){
    }

    @Before("loginPointCut()")
    public void testToken(){
        RestClient restClient = new RestClient("http://localhost:8082/api/test");
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = servletRequest.getHeader("Authorization");
        if(!restClient.testToken(token)){
            throw new RuntimeException();
        }
    }

}
