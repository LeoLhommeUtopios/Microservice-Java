package org.example.demofeignclient.service;

import org.example.demofeignclient.client.ProductClient;
import org.example.demofeignclient.dto.CreateStockRequest;
import org.example.demofeignclient.dto.ProductDto;
import org.example.demofeignclient.model.Stock;
import org.example.demofeignclient.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);
    private final ProductClient productClient;

    private StockRepository repository;
    private ProductClient client;

    public StockService(StockRepository repository, ProductClient client, ProductClient productClient){
        this.repository = repository;
        this.client = client;
        this.productClient = productClient;
    }

    public Stock create (CreateStockRequest request){

        if(client.productExist(request.idProduct())){
            Stock stock = Stock.builder().productDtoId(request.idProduct()).quantity(request.quantity()).build();
            repository.save(stock);
            return stock;
        }
        throw new RuntimeException("Product Not found");

    }
}
