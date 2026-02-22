package com.samar.inventory.service;

import com.samar.inventory.dao.InventoryRepository;
import com.samar.inventory.dto.InventoryDto;

import com.samar.inventory.model.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Boolean addInventory(InventoryDto inventoryDto) {
        try {
            if(inventoryRepository.existsById(inventoryDto.getInventoryId())){
                System.out.println("inventory id already in use by another product ");
                return false;
            }
            Inventory inventory=new Inventory();
            inventory.setQuantity(inventoryDto.getQuantity());
            inventory.setProductId(inventoryDto.getProductId());
            System.out.println("check");
            inventoryRepository.save(inventory);
            System.out.println("check");
                return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public InventoryDto getProductFromInventoryByProductId(int id) {
        Inventory inventory=inventoryRepository.findByProductId(id);
        InventoryDto inventoryDto=new InventoryDto();
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setProductId(inventory.getProductId());
        inventoryDto.setInventoryId(inventory.getInventoryId());
        return inventoryDto;
    }

    @Transactional
    public Boolean updateInventory(InventoryDto inventoryDto, int id) {
        try{
            Optional<Inventory> inventory= Optional.ofNullable(inventoryRepository.findByProductId(id));
            if (inventory.isPresent()) {
                inventory.get().setProductId(inventoryDto.getProductId()!=0?inventoryDto.getProductId():inventory.get().getProductId());
                inventory.get().setQuantity(inventoryDto.getQuantity()!=0?inventoryDto.getQuantity():inventory.get().getQuantity());
                inventory.get().setInventoryId(inventoryDto.getInventoryId() != 0 ? inventoryDto.getInventoryId() : inventory.get().getInventoryId());
                inventoryRepository.save(inventory.get());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Transactional
    public Boolean deleteInventory(int id) {
        try{
            inventoryRepository.deleteByProductId(id);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
