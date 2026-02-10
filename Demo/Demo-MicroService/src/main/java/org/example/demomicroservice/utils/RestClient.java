package org.example.demomicroservice.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class RestClient {

    private String urlApi;
    private RestTemplate template;
    private HttpHeaders httpHeaders;

    public RestClient (String urlApi){
        this.urlApi = urlApi;
        template = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept","*/*");
        httpHeaders.add("content-type","application/json");
    }

    public boolean testToken (String token){
        httpHeaders.add("Authorization",token);
        HttpEntity<String> requestEntity = new HttpEntity<>("",httpHeaders);
        ResponseEntity<String> response = template.exchange(urlApi, HttpMethod.GET,requestEntity,String.class);
        if(response.hasBody()){
            return response.getStatusCode().is2xxSuccessful();
        }
        return false;
    }


}
