package org.example.demofeignclient.client;

import org.example.demofeignclient.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProductClientFallback implements ProductClient{

    private static Logger log = LoggerFactory.getLogger(ProductClientFallback.class);

    @Override
    public List<ProductDto> findAll() {
        log.warn("Fallback: Product service indisponible pour findAll");

        return Collections.emptyList();
    }

    @Override
    public ProductDto findById(Long id) {
        log.warn("Fallback: Product service indisponible pour l'id {}",id);

        return new ProductDto(
                id,
                "Unknow",
                0.0
        );
    }

    @Override
    public Boolean productExist(long id) {
        return false;
    }
}
