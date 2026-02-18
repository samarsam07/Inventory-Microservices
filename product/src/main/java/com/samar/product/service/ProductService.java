package com.samar.product.service;


import com.samar.product.dao.ProductRepository;
import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.feign.ProductFeignClient;
import com.samar.product.mapper.InventoryMapper;
import com.samar.product.mapper.ProductMapper;
import com.samar.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeignClient productFeignClient;

    final private ProductMapper productMapper=new ProductMapper();
    final private InventoryMapper inventoryMapper=new InventoryMapper();

    public List<ProductDto> getAllProduct() {
        List<Product> products=productRepository.findAll();
        List<InventoryDto> inventoryDtos=productFeignClient.getAllFromInventory().getBody();
        List<ProductDto> productDtos=new ArrayList<>();
        for(int i=0;i<inventoryDtos.size();i++){
            ProductDto productDto=new ProductDto();
            if(inventoryDtos.get(i).getProductId()==products.get(i).getProductId()){
                productDtos.add(productMapper.productToDto(inventoryDtos.get(i),products.get(i)));
            }
        }
        return productDtos;
    }

    public  ProductDto getProductById(int id) {
        Optional<Product> product
                = productRepository.findById(id);
        InventoryDto inventoryDto=productFeignClient.getProductFromInventoryByProductId(id).getBody();
        ProductDto productDto=new ProductDto();
        if (product.isPresent()&& inventoryDto!=null){
            return productMapper.productToDto(inventoryDto,product.get());
        }
        return null;
    }

    public Boolean addProduct(ProductDto productDto) {
        try{
        Product product=productMapper.dtoToProduct(productDto);
        InventoryDto inventoryDto= inventoryMapper.productDtoToInventory(productDto);
        ResponseEntity<?> res= productFeignClient.addInventory(inventoryDto);
        productRepository.save(product);
        return true;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getProductQuantity(int id) {
        InventoryDto inventoryDto=productFeignClient.getProductFromInventoryByProductId(id).getBody();
        if(inventoryDto!=null)
            return inventoryDto.getQuantity();
        return  -1;
    }

/*
    public boolean updateProductById(int id, ProductDto productDto) {
        Optional<Product> old=productRepository.findById(id);
        if(old.isPresent()){
            old.get().setCategory(productDto.getCategory() != null ? productDto.getCategory():old.get().getCategory());
            old.get().setProductName(productDto.getProductName()!=null ? productDto.getProductName() : old.get().getProductName());
            old.get().setProductPrice(productDto.getProductPrice()!=0?productDto.getProductPrice():old.get().getProductPrice());
            old.get().setDescription(productDto.getDescription()!=null
            ?productDto.getDescription():old.get().getDescription());
            productRepository.save(old.get());
            return true;
        }
        return false;
    }

    public boolean deleteProductById(int id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
*/
}
