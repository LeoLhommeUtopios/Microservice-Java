package org.example.demomicroservice.controller;

import jakarta.validation.Valid;
import org.example.demomicroservice.dto.CreateProductRequest;
import org.example.demomicroservice.dto.ProductDto;
import org.example.demomicroservice.dto.UpdateProductRequest;
import org.example.demomicroservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    public ProductController (ProductService productService){
        service = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll (){
        log.info("GET /api/products - recuperation de tous les produits");
        List<ProductDto> productDtos = service.findAll();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById (@PathVariable Long id){
        log.info("GET /api/products/{} - recuperation d'un produit",id);
        ProductDto productDto = service.findById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<Boolean> productExist(@PathVariable long id){
        return ResponseEntity.ok(service.productExist(id));
    }
}
