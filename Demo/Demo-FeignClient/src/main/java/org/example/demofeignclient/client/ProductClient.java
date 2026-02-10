package org.example.demofeignclient.client;


import org.example.demofeignclient.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8081",
        fallback = ProductClientFallback.class
)
public interface ProductClient {

    @GetMapping("/api/products")
    public List<ProductDto> findAll ();

    @GetMapping("/api/products/{id}")
    public ProductDto findById (@PathVariable Long id);

    @GetMapping("/api/products/exist/{id}")
    public Boolean productExist(@PathVariable long id);



}
