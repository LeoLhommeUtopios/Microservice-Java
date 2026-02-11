package org.example.demomicroservice.controller;

import jakarta.validation.Valid;
import org.example.demomicroservice.annotation.Login;
import org.example.demomicroservice.dto.CreateProductRequest;
import org.example.demomicroservice.dto.ProductDto;
import org.example.demomicroservice.dto.UpdateProductRequest;
import org.example.demomicroservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class LoggedController {


    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    public LoggedController (ProductService productService){
        service = productService;
    }

    @Login
    @PostMapping
    public ResponseEntity<ProductDto> create (@Valid @RequestBody CreateProductRequest request){
        log.info("Post /api/product creation d'un produit");
        ProductDto createdProduct = service.create(request);

        URI location = URI.create("/api/products/"+createdProduct.id());
        return ResponseEntity.created(location).body(createdProduct);
    }
    @Login
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update (@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        log.info("PUT /api/products/{} - mise a jour complete",id);
        ProductDto productDto= service.update(id,request);
        return ResponseEntity.ok(productDto);
    }
    @Login
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updatePartial (@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        log.info("Patch /api/products/{} - mise a jour partiel",id);
        ProductDto productDto= service.update(id,request);
        return ResponseEntity.ok(productDto);
    }
}
