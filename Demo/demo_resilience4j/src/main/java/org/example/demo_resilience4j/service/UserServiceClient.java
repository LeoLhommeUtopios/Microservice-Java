package org.example.demo_resilience4j.service;

import org.example.demo_resilience4j.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private int callcount = 0;

    public UserServiceClient(){
        this.restTemplate = new RestTemplate();
    }

    public User getUserById  (long id){
        callcount ++;
        System.out.println("Appel # "+callcount+" au user service pour l'utilisateur a l'id "+id);

        if (callcount < 5){
            System.out.println("simulation d'une erreure reseau");
            throw new RuntimeException("User service indisponible");
        }

        System.out.println("user service repond correctement");
        return new User(id,"toto","toto@email.com");
    }

    public User getSlowUser(long id){
        System.out.println("appel au service lent ");
        try{
            Thread.sleep(3000);
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        return new User(id,"slow user","user@email.com");
    }

    public void resetCount(){
        callcount = 0;
    }

}
