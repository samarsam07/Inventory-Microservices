package com.samar.product.controller;


import com.samar.product.dto.ProductDto;
import com.samar.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(),HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int id){
        ProductDto productDto=productService.getProductById(id);
        if (productDto==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }




    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto){
        productService.addProduct(productDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

/*
    @GetMapping("productname/{name}")
        public ResponseEntity<ProductDto> getProductByName(@PathVariable String name){
            ProductDto productDto=productService.getProductByName(name);
            if (productDto==null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(productDto,HttpStatus.OK);
        }
    @PutMapping("/update/id/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable int id,@RequestBody ProductDto productDto){
        boolean check= productService.updateProductById(id,productDto);

        if(!check)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable int id){
        boolean check= productService.deleteProductById(id);
        if (!check)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
*/

}
