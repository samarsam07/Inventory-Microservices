package com.samar.product.feign;

import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("INVENTORY-SERVICE")
public interface ProductFeignClient {
    @PostMapping("/inventory/add")
    public ResponseEntity<?> addInventory(@RequestBody InventoryDto inventoryDto);

    @GetMapping("inventory")
    public ResponseEntity<List<InventoryDto>> getAllFromInventory();

    @GetMapping("/inventory/productid/{id}")
    public ResponseEntity<InventoryDto> getProductFromInventoryByProductId(@PathVariable int id);

    @GetMapping("/inventory/id/{id}")
    public  ResponseEntity<InventoryDto> getProductFromInventoryById(@PathVariable int id);
}
