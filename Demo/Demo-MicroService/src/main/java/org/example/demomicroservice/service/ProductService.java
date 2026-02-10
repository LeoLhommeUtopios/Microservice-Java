package org.example.demomicroservice.service;

import jakarta.transaction.Transactional;
import org.apache.catalina.UserDatabase;
import org.example.demomicroservice.dto.CreateProductRequest;
import org.example.demomicroservice.dto.ProductDto;
import org.example.demomicroservice.dto.UpdateProductRequest;
import org.example.demomicroservice.exception.NotFoundException;
import org.example.demomicroservice.exception.ProductAlreadyExist;
import org.example.demomicroservice.mapper.ProductMapper;
import org.example.demomicroservice.model.Product;
import org.example.demomicroservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    private final ProductMapper mapper;

    public ProductService (ProductRepository repository,ProductMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductDto> findAll (){
        log.debug("Récuperation de tous les produits");
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ProductDto findById(Long id){
        log.debug("Récuperation d'un produit par sont id");
        return repository.findById(id).map(mapper::toDto).orElseThrow(()-> new NotFoundException(id.toString()));
    }

    @Transactional
    public ProductDto create (CreateProductRequest request){
        log.info("Création d'un produit : {}",request.name());

        if(repository.existsByName(request.name()) ){
            throw new ProductAlreadyExist(request.name());
        }

        Product product = mapper.toEntity(request);
        Product productSave = repository.save(product);

        log.info("Produit crée avec l'id : "+productSave.getId());
        return mapper.toDto(productSave);
    }

    @Transactional
    public ProductDto update (Long id, UpdateProductRequest request){
        log.info("Mise a jour du produit a l'id : "+id);
        Product product = repository.findById(id).orElseThrow(()->new NotFoundException(id.toString()));

        if(request.name() != null && !request.name().equals(product.getName())){
            if(repository.existsByName(request.name())){
                throw new ProductAlreadyExist(request.name());
            }
        }

        mapper.updateEntity(product,request);
        Product productSave = repository.save(product);

        log.info("Produit mis a jour: {}",productSave.getId());
        return mapper.toDto(productSave);
    }

    public boolean productExist (Long id){
        repository.findById(id).orElseThrow(()-> new NotFoundException(id.toString()));
        return true;
    }

}
