package com.samar.product.service;


import com.samar.product.dao.ProductRepository;
import com.samar.product.dto.InventoryDto;
import com.samar.product.dto.ProductDto;
import com.samar.product.exception.InventoryException;
import com.samar.product.feign.ProductFeignClient;
import com.samar.product.mapper.InventoryMapper;
import com.samar.product.mapper.ProductMapper;
import com.samar.product.model.Product;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    ProductFeignClient productFeignClient;

    final private ProductMapper productMapper=new ProductMapper();
    final private InventoryMapper inventoryMapper=new InventoryMapper();

    public List<ProductDto> getAllProduct() {
        List<Product> products=productRepository.findAll();
        List<ProductDto> productDtos=new ArrayList<>();
        for(Product product:products){
            ProductDto productDto=new ProductDto();
            InventoryDto inventoryDto=productFeignClient.getProductFromInventoryByProductId(product.getProductId()).getBody();
            if(inventoryDto!=null){
                productDtos.add(productMapper.productToDto(inventoryDto,product));
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
        try {
            if (productRepository.existsById(productDto.getProductId())) {
                System.out.println("primary key already ezist try with another id");
                return false;
            }

            Product product = productMapper.productDtoToDao(productDto);
            InventoryDto inventoryDto = inventoryMapper.productDtoToInventory(productDto);
            Product product1 = productRepository.save(product);
            inventoryDto.setProductId(product1.getProductId());
            Boolean res = productFeignClient.addInventory(inventoryDto).getBody();
            System.out.println(res + " 1");
            if (res == null || !res)
                throw new InventoryException("Inventory Fail");
            System.out.println("2");
            System.out.println("3");
            return true;
        }
        catch (InventoryException e) {
            System.out.println("ERROR: "+e.getMessage());
            productFeignClient.deleteInventory(productDto.getProductId());
        }catch(Exception e){
            System.out.println("error"+" "+e.getMessage());
        }
        return false;
    }

    public int getProductQuantity(int id) {
        InventoryDto inventoryDto=productFeignClient.getProductFromInventoryByProductId(id).getBody();
        if(inventoryDto!=null)
            return inventoryDto.getQuantity();
        return  -1;
    }
    @Transactional
    public boolean updateProductById(int id, ProductDto productDto) {
        Optional<Product> oldOpt = productRepository.findById(id);

        if(oldOpt.isEmpty())
            return false;

        Product oldProduct = oldOpt.get();

        Product backup = new Product();
        backup.setProductId((oldProduct.getProductId()));
        backup.setCategory(oldProduct.getCategory());
        backup.setProductName(oldProduct.getProductName());
        backup.setProductPrice(oldProduct.getProductPrice());
        backup.setDescription(oldProduct.getDescription());
        try{
            oldProduct.setCategory(productDto.getCategory()!=null ? productDto.getCategory():oldProduct.getCategory());
            oldProduct.setProductName(productDto.getProductName()!=null ? productDto.getProductName():oldProduct.getProductName());
            oldProduct.setProductPrice(productDto.getProductPrice()!=0 ? productDto.getProductPrice():oldProduct.getProductPrice());
            oldProduct.setDescription(productDto.getDescription()!=null ? productDto.getDescription():oldProduct.getDescription());
            productRepository.save(oldProduct);
            Boolean check = productFeignClient
                    .updateInventory(inventoryMapper.productDtoToInventory(productDto),id)
                    .getBody();
            if(check==null || !check){
                throw new RuntimeException("Inventory update failed");
            }

            return true;

        }catch(Exception e){
            System.out.println("Rollback product: "+e.getMessage());
            productRepository.save(backup);
            return false;
        }
    }

    @Transactional
    public boolean deleteProductById(int id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if(productOpt.isEmpty())
            return false;

        Product backup = productOpt.get();
        InventoryDto backupDto=productFeignClient.getProductFromInventoryByProductId(backup.getProductId()).getBody();

        try{
            Boolean inv = productFeignClient.deleteInventory(id).getBody();

            if(inv==null || !inv){
                throw new RuntimeException("Inventory delete failed");
            }

            productRepository.deleteById(id);

            return true;

        }catch(Exception e){
            System.out.println("Delete failed: "+e.getMessage());

            productFeignClient.addInventory(backupDto);

            return false;
        }
    }

}
