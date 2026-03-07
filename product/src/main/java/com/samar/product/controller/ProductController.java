package com.samar.product.controller;


import com.samar.product.dto.ProductDto;
import com.samar.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("productId/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int id){
        ProductDto productDto=productService.getProductById(id);
        if(productDto==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Boolean> addProduct(@RequestBody ProductDto productDto){
        boolean check=productService.addProduct(productDto);
        if (!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
    }
    @PutMapping("productId/{id}")
    public ResponseEntity<Boolean> updateProduct(@RequestBody ProductDto productDto,@PathVariable int id){
        boolean check=productService.updateProduct(productDto,id);
        if (!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("productId/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable int id){
        boolean check=productService.deleteProduct(id);
        if (!check)
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

}