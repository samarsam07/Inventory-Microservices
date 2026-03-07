package com.samar.order_service.feign;

import com.samar.order_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PRODUCT-SERVICE")
public interface ProductFeignClient {

    @GetMapping("product/productId/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int id);


}