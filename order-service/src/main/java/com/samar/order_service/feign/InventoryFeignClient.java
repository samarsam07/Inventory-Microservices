package com.samar.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryFeignClient {

    @PostMapping("inventory/quantity/productId/{id}")
    public ResponseEntity<Boolean> checkQuantity(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int quantity);

    @PutMapping("inventory/restore/{productId}/{quantity}")
    public ResponseEntity<Boolean> restoreQuantity(
            @PathVariable int productId,
            @PathVariable int quantity);
}
