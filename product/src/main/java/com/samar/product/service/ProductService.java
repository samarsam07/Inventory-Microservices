package com.samar.product.service;


import com.samar.product.dao.ProductRepository;
import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.feign.ProductFeignClient;
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

    public List<ProductDto> getAllProduct() {
        List<Product> products=productRepository.findAll();
        List<InventoryDto> inventoryDtos=productFeignClient.getAllFromInventory().getBody();

        List<ProductDto> productDtos=new ArrayList<>();
        for(int i=0;i<inventoryDtos.size();i++){
            ProductDto productDto=new ProductDto();
            if(inventoryDtos.get(i).getProductId()==products.get(i).getProductId()){
                productDto.setProductName(products.get(i).getProductName());
                productDto.setProductPrice(products.get(i).getProductPrice());
                productDto.setProductId(products.get(i).getProductId());
                productDto.setCategory(products.get(i).getCategory());
                productDto.setDescription(products.get(i).getDescription());
                productDto.setQuantity(inventoryDtos.get(i).getQuantity());
                productDto.setInventoryId(inventoryDtos.get(i).getInventoryId());
                productDtos.add(productDto);
            }
        }
        return productDtos;
    }

    public  ProductDto getProductById(int id) {
        Optional<Product> product
                = productRepository.findById(id);
        InventoryDto inventoryDto=productFeignClient.getProductFromInventoryByProductId(id).getBody();
        ProductDto productDto=new ProductDto();
        if (product.isPresent()){
            productDto.setProductName(product.get().getProductName());
            productDto.setProductPrice(product.get().getProductPrice());
            productDto.setProductId(product.get().getProductId());
            productDto.setCategory(product.get().getCategory());
            productDto.setDescription(product.get().getDescription());
            if(inventoryDto!=null){
                productDto.setQuantity(inventoryDto.getQuantity());
                productDto.setInventoryId(inventoryDto.getInventoryId());
            }else{
                return null;
            }

        }else {
            return null;
        }

        return productDto;

    }

    public void addProduct(ProductDto productDto) {
        InventoryDto inventoryDto=new InventoryDto();
        Product product=new Product();
        inventoryDto.setProductId(productDto.getProductId());
        inventoryDto.setQuantity(productDto.getQuantity());


        product.setDescription(productDto.getDescription());
        product.setProductPrice(productDto.getProductPrice());
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());

        try{
        ResponseEntity<?> res= productFeignClient.addInventory(inventoryDto);
        productRepository.save(product);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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
