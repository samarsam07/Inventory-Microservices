package com.samar.inventory.service;

import com.samar.inventory.dao.InventoryRepository;
import com.samar.inventory.dto.InventoryDto;

import com.samar.inventory.model.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;


    public List<InventoryDto> getAllFromInventory() {
        List<Inventory>inventories=inventoryRepository.findAll();
        List<InventoryDto> inventoryDtos=new ArrayList<>();
        for (Inventory inventory:inventories){
            InventoryDto inventoryDto= new InventoryDto();
            inventoryDto.setInventoryId(inventory.getInventoryId());
            inventoryDto.setProductId(inventory.getProductId());
            inventoryDto.setQuantity(inventory.getQuantity());
            inventoryDtos.add(inventoryDto);
        }
        return inventoryDtos;
    }

    public InventoryDto getProductFromInventoryById(int id) {
        InventoryDto inventoryDto=new InventoryDto();
        Optional<Inventory> inventory=inventoryRepository.findById(id);
        if (inventory.isPresent()){
            inventoryDto.setInventoryId(inventory.get().getInventoryId());
            inventoryDto.setProductId(inventory.get().getProductId());
            inventoryDto.setQuantity(inventory.get().getQuantity());
        }
        return inventoryDto;
    }

    public void addInventory(InventoryDto inventoryDto) {
        Inventory inventory=new Inventory();
        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setProductId(inventoryDto.getProductId());
        inventoryRepository.save(inventory);
    }

    public InventoryDto getProductFromInventoryByProductId(int id) {
        Inventory inventory=inventoryRepository.findByProductId(id);
        InventoryDto inventoryDto=new InventoryDto();
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setProductId(inventory.getProductId());
        inventoryDto.setInventoryId(inventory.getInventoryId());
        return inventoryDto;
    }
}
