package com.samar.order_service.controller;


import com.samar.order_service.dto.OrderDto;
import com.samar.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order")
public class orderController {

    @Autowired
    OrderService orderService;
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrder(){
        List<OrderDto> orderDtos=orderService.getAllOrder();
        if(orderDtos.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(orderDtos,HttpStatus.OK);
    }

}
