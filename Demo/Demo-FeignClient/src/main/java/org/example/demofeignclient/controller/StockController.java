package org.example.demofeignclient.controller;

import org.example.demofeignclient.dto.CreateStockRequest;
import org.example.demofeignclient.model.Stock;
import org.example.demofeignclient.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Stock> createStock (@RequestBody CreateStockRequest request){

        Stock stock = service.create(request);
        URI location = URI.create("/api/stock/"+stock.getId());
        return ResponseEntity.created(location).body(stock);

    }


}
