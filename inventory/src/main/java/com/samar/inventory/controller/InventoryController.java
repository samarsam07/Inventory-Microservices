package com.samar.inventory.controller;

import com.samar.inventory.dto.InventoryDto;

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
    public ResponseEntity<List<InventoryDto>> getAllInventory(){
        return new ResponseEntity<>(inventoryService.getAllInventory(),HttpStatus.OK);
    }

    @GetMapping("inventoryId/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable int id){
        InventoryDto inventoryDto=inventoryService.getInventoryById(id);
        if (inventoryDto==null){
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(inventoryDto,HttpStatus.OK);
    }
    @GetMapping("productId/{id}")
    public ResponseEntity<InventoryDto> getInventoryByProductId(@PathVariable int id){
        InventoryDto inventoryDto=inventoryService.getInventoryByProductId(id);
        if (inventoryDto==null)
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(inventoryDto,HttpStatus.OK);
    }
    @PostMapping("add")
    public ResponseEntity<Boolean> addProductInInventory(@RequestBody InventoryDto inventoryDto){
        Boolean check=inventoryService.addProductInInventory(inventoryDto);
        if(!check) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(true,HttpStatus.CREATED);
    }
    @PutMapping("productId/{id}")
    public ResponseEntity<Boolean> updateInventory(@RequestBody InventoryDto inventoryDto,@PathVariable int id){
        Boolean check=inventoryService.updateInventory(inventoryDto,id);
        if (!check)
            return  new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return  new ResponseEntity<>(true,HttpStatus.ACCEPTED);
    }
    @PostMapping("quantity/productId/{id}")
    public ResponseEntity<Boolean> checkQuantity(@PathVariable int id,@RequestParam(defaultValue = "0") int quantity){
        Boolean check=inventoryService.checkQuantity(id,quantity);
        if(!check)
            return new ResponseEntity<>(false,HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PutMapping("/restore/{productId}/{quantity}")
    public ResponseEntity<Boolean> restoreQuantity(
            @PathVariable int productId,
            @PathVariable int quantity) {

        boolean restored = inventoryService.restore(productId, quantity);

        if (!restored)
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/productId/{productId}")
    public ResponseEntity<Boolean> deleteInventory(
            @PathVariable int productId) {

        boolean deleted = inventoryService.deleteInventory(productId);

        if (!deleted)
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

