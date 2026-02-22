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
        Boolean check=inventoryService.addInventory(inventoryDto);
        if(!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PutMapping("update/productId/{id}")
    public ResponseEntity<Boolean> updateInventory(@RequestBody InventoryDto inventoryDto,@PathVariable int id){
        Boolean check=inventoryService.updateInventory(inventoryDto,id);
        if(!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @DeleteMapping("delete/productId/{id}")
    public ResponseEntity<Boolean> deleteInventory(@PathVariable int id){
        Boolean check=inventoryService.deleteInventory(id);
        if(!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

}
