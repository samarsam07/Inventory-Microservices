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

    public List<InventoryDto> getAllInventory() {
        List<Inventory> inventories=inventoryRepository.findAll();
        List<InventoryDto> inventoryDtos=new ArrayList<>();
        for(Inventory inventory:inventories){
            InventoryDto inventoryDto=new InventoryDto();
            inventoryDto.setInventoryId(inventory.getInventoryId());
            inventoryDto.setQuantity(inventory.getQuantity());
            inventoryDto.setProductId(inventory.getProductId());
            inventoryDtos.add(inventoryDto);
        }
        return  inventoryDtos;
    }


    public InventoryDto getInventoryById(int id) {
        Optional<Inventory> inventory=inventoryRepository.findById(id);
        InventoryDto inventoryDto=new InventoryDto();
        if(inventory.isPresent()){
            inventoryDto.setProductId(inventory.get().getProductId());
            inventoryDto.setQuantity(inventory.get().getQuantity());
            inventoryDto.setInventoryId(inventory.get().getInventoryId());
            return inventoryDto;
        }

        return  null;
    }
    public  InventoryDto getInventoryByProductId(int id){
        try{
            Inventory inventory=inventoryRepository.findByProductId(id);
            if (inventory==null)
                return null;
            InventoryDto inventoryDto=new InventoryDto();
            inventoryDto.setInventoryId(inventory.getInventoryId());
            inventoryDto.setQuantity(inventory.getQuantity());
            inventoryDto.setProductId(inventory.getProductId());

            return inventoryDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public Boolean addProductInInventory(InventoryDto inventoryDto) {
        if(inventoryRepository.existsByProductId(inventoryDto.getProductId()))
            return false;
        try{
            Inventory inventory=new Inventory();
            inventory.setProductId(inventoryDto.getProductId());
            inventory.setQuantity(inventoryDto.getQuantity());
            inventoryRepository.save(inventory);
            return  true;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Transactional
    public Boolean updateInventory(InventoryDto inventoryDto, int id) {
        try{
            Optional<Inventory> inventory= Optional.ofNullable(inventoryRepository.findByProductId(id));
            if(inventory.isPresent()){
                inventory.get().setQuantity(inventoryDto.getQuantity()!=0?inventoryDto.getQuantity():inventory.get().getQuantity());
                inventory.get().setProductId(inventoryDto.getProductId()!=0?inventoryDto.getProductId():inventory.get().getProductId());
                inventory.get().setInventoryId(inventoryDto.getInventoryId()!=0?inventoryDto.getInventoryId():inventory.get().getInventoryId());
                inventoryRepository.save(inventory.get());
                return true;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Transactional
    public Boolean deleteInventory(int productId) {

        try {
            Inventory inventory = inventoryRepository.findByProductId(productId);

            if (inventory == null) {
                System.out.println("Inventory not found");
                return false;
            }

            inventoryRepository.delete(inventory);

            System.out.println("Inventory deleted successfully");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean checkQuantity(int id, Integer quantity) {
        Inventory inventory= inventoryRepository.findByProductId(id);
        System.out.println(inventory);
        if(inventory==null)
            return  false;

        if(inventory.getQuantity()<quantity)
            return false;
//        Inventory inv=new Inventory();
//        inv.setQuantity(inventory.get().getQuantity()-quantity);
//        inv.setProductId(inventory.get().getProductId());
//        inv.setInventoryId(inventory.get().getInventoryId());
        inventory.setQuantity(inventory.getQuantity()-quantity);
        inventoryRepository.save(inventory);
        System.out.println("DOne");
        return true;
    }
    public void save(Inventory inventory){
        inventoryRepository.save(inventory);
    }

    @Transactional
    public boolean restore(int productId, int quantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId);

        if (inventory == null)
            return false;

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);

        return true;
    }
}
