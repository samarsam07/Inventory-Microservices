package com.samar.product.feign;

import com.samar.product.dto.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient("INVENTORY-SERVICE")
public interface InventoryFeignClient {

    @GetMapping("inventory/productId/{id}")
    public ResponseEntity<InventoryDto> getInventoryByProductId(@PathVariable("id") int id);

    @PostMapping("inventory/add")
    public ResponseEntity<Boolean> addProductInInventory(@RequestBody InventoryDto inventoryDto);

    @PutMapping("inventory/productId/{id}")
    public ResponseEntity<Boolean> updateInventory(@RequestBody InventoryDto inventoryDto,@PathVariable int id);

    @DeleteMapping("inventory/productId/{id}")
    public  ResponseEntity<Boolean> deleteInventory(@PathVariable("id") int id);
}
