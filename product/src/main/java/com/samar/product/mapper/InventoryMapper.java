package com.samar.product.mapper;

import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.model.Product;

public class InventoryMapper {
    public InventoryDto productDtoToInventory(ProductDto productDto){
        InventoryDto inventoryDto=new InventoryDto();
        inventoryDto.setInventoryId(productDto.getInventoryId());
        inventoryDto.setProductId(productDto.getProductId());
        inventoryDto.setQuantity(productDto.getQuantity());
        return inventoryDto;
    }

}
