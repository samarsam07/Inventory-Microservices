package com.samar.product.service;


import com.samar.product.dao.ProductRepository;
import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.exception.InventoryException;
import com.samar.product.feign.InventoryFeignClient;
import com.samar.product.mapper.InventoryMapper;
import com.samar.product.mapper.ProductMapper;
import com.samar.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryFeignClient inventoryFeignClient;


    final ProductMapper productMapper=new ProductMapper();
    final InventoryMapper inventoryMapper=new InventoryMapper();


    public List<ProductDto> getAllProduct() {
        List<Product> products=productRepository.findAll();
        List<ProductDto> productDtos=new ArrayList<>();
        for(Product product:products){
            InventoryDto inventoryDto= inventoryFeignClient.getInventoryByProductId(product.getProductId()).getBody();
            if (inventoryDto!=null)
                productDtos.add(productMapper.productToDto(product,inventoryDto));
        }
        return productDtos;
    }

    public ProductDto getProductById(int id) {
        try {
            Optional<Product> product=productRepository.findById(id);
            if (product.isPresent()){
                InventoryDto inventoryDto= inventoryFeignClient.getInventoryByProductId(product.get().getProductId()).getBody();
                if (inventoryDto!=null)
                    return productMapper.productToDto(product.get(),inventoryDto);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean addProduct(ProductDto productDto) {
        int dummyProductId=-1;
        try {
            Product product=productMapper.dtoToProduct(productDto);
            InventoryDto inventoryDto=inventoryMapper.productDtoToInventory(productDto);
            Product product1=productRepository.save(product);
            inventoryDto.setProductId(product1.getProductId());
            dummyProductId=product1.getProductId();
            Boolean check= inventoryFeignClient.addProductInInventory(inventoryDto).getBody();
            if(check==null || !check)
                throw  new InventoryException("Inventory fail");

            // SalesForceProduct p = new SalesForceProduct();
            // p.setName(product.getProductName());
            // p.setIsActive(true);
            // p.setDescription(product.getDescription());

            // salesforceClient.createRecord("Product2", p);
            return true;
        }catch (InventoryException e){
            System.out.println(e.getMessage());
            productRepository.deleteById(dummyProductId);
        }catch (Exception e){
            inventoryFeignClient.getInventoryByProductId(dummyProductId);
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Transactional
    public boolean updateProduct(ProductDto productDto, int id) {
        Optional<Product> old=productRepository.findById(id);
        if(old.isEmpty())
            return false;
        Product backup=old.get();
        try{
            old.get().setProductName(productDto.getProductName()!=null? productDto.getProductName() : old.get().getProductName());
            old.get().setProductPrice(productDto.getProductPrice()!=0?productDto.getProductPrice():old.get().getProductPrice());
            old.get().setCategory(productDto.getCategory()!=null? productDto.getCategory() : old.get().getCategory());
            old.get().setDescription(productDto.getDescription()!=null?productDto.getDescription():old.get().getDescription());
            old.get().setProductId(productDto.getProductId()!=0? productDto.getProductId() : old.get().getProductId());
            productRepository.save(old.get());
            Boolean check= inventoryFeignClient.updateInventory(inventoryMapper.productDtoToInventory(productDto),id).getBody();
            if(check==null || !check)
                throw new InventoryException("failed");
            return true;
        }catch (InventoryException e){
            productRepository.save(backup);
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Transactional
    public boolean deleteProduct(int id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            System.out.println("Product not found");
            return false;
        }

        Product product = optionalProduct.get();

        InventoryDto backupInventory = null;

        try {
            backupInventory = inventoryFeignClient
                    .getInventoryByProductId(product.getProductId())
                    .getBody();


            Boolean inventoryDeleted = inventoryFeignClient
                    .deleteInventory(id)
                    .getBody();

            if (inventoryDeleted == null || !inventoryDeleted) {
                throw new RuntimeException("Failed to delete inventory");
            }


            productRepository.deleteById(id);

            System.out.println("Product deleted successfully");
            return true;

        } catch (Exception e) {

            e.printStackTrace();

            if (backupInventory != null) {
                inventoryFeignClient.addProductInInventory(backupInventory);
                System.out.println("Inventory restored due to failure");
            }

            return false;
        }
    }
}
