package com.samar.product.mapper;

import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.model.Product;


public class ProductMapper {
    public ProductDto productToDto(Product product, InventoryDto inventoryDto){
        ProductDto productDto=new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setCategory(product.getCategory());
        productDto.setQuantity(inventoryDto.getQuantity());
        productDto.setInventoryId(inventoryDto.getInventoryId());
        productDto.setDescription(product.getDescription());
        return  productDto;
    }
    public Product dtoToProduct(ProductDto productDto){
        Product product=new Product();
        product.setProductId(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setProductPrice(productDto.getProductPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        return product;
    }


}
