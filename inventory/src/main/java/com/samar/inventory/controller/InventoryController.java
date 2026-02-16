package com.samar.inventory.controller;

import com.samar.inventory.dto.InventoryDto;
import com.samar.inventory.dto.ProductDto;
import com.samar.inventory.service.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAllFromInventory(){
        return new ResponseEntity<>(inventoryService.getAllFromInventory(), HttpStatus.OK);
    }
    @GetMapping("id/{id}")
    public  ResponseEntity<InventoryDto> getProductFromInventoryById(@PathVariable int id){
        return new ResponseEntity<>(inventoryService.getProductFromInventoryById(id),HttpStatus.OK);
    }
    @GetMapping("productid/{id}")
    public ResponseEntity<InventoryDto> getProductFromInventoryByProductId(@PathVariable int id){
        return new ResponseEntity<>(inventoryService.getProductFromInventoryByProductId(id),HttpStatus.OK);
    }
    @PostMapping("add")
    public ResponseEntity<?> addInventory(@RequestBody InventoryDto inventoryDto){
        inventoryService.addInventory(inventoryDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
