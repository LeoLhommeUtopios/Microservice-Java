package org.example.demomicroservice.mapper;

import org.example.demomicroservice.dto.CreateProductRequest;
import org.example.demomicroservice.dto.ProductDto;
import org.example.demomicroservice.dto.UpdateProductRequest;
import org.example.demomicroservice.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto (Product product){
        if(product == null){
            return null;
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public Product toEntity (CreateProductRequest request){
        if(request == null){
            return null;
        }

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .build();

        return product;
    }

    public void updateEntity(Product product, UpdateProductRequest request){
        if(request == null){
            return;
        }

        if(request.name() != null){
            product.setName(request.name());
        }
        if (request.price() != null){
            product.setPrice(request.price());
        }
    }

}
