package com.samar.product.mapper;

import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.model.Product;

public class ProductMapper {
    public ProductDto productToDto(InventoryDto inventoryDto, Product product){
        ProductDto productDto=new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductId(product.getProductId());
        productDto.setCategory(product.getCategory());
        productDto.setDescription(product.getDescription());
        productDto.setQuantity(inventoryDto.getQuantity());
        productDto.setInventoryId(inventoryDto.getInventoryId());
        return productDto;
    }

    public Product dtoToProduct(ProductDto productDto){
        Product product=new Product();
        product.setDescription(productDto.getDescription());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());
        return product;
    }
}
