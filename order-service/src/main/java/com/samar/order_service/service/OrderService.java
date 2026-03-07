package com.samar.order_service.service;

import com.samar.order_service.dao.OrderRepository;
import com.samar.order_service.dto.OrderDto;
import com.samar.order_service.dto.ProductDto;
import com.samar.order_service.feign.InventoryFeignClient;
import com.samar.order_service.feign.ProductFeignClient;
import com.samar.order_service.mapper.OrderMapper;
import com.samar.order_service.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private InventoryFeignClient inventoryFeignClient;
    @Autowired
    OrderRepository orderRepository;
    final OrderMapper orderMapper=new OrderMapper();

    public List<OrderDto> getAllOrder() {
        List<Orders> orders=orderRepository.findAll();
        List<OrderDto> orderDtos=new ArrayList<>();
        for(Orders order:orders){
            orderDtos.add(orderMapper.orderToDto(order));
        }
        return orderDtos;
    }

    public OrderDto getOrderById(int orderId) {
        Optional<Orders> order=orderRepository.findById(orderId);
        return order.map(orderMapper::orderToDto).orElse(null);

    }

    @Transactional
    public Boolean addOrder(OrderDto orderDto) {
        try{
            ProductDto productDto=productFeignClient.getProductById(orderDto.getProductId()).getBody();
            System.out.println(productDto);
            System.out.println("1");
            if(productDto==null) {
                System.out.println("product is null");
                return false;
            }
            System.out.println("2");
            Boolean check=inventoryFeignClient.checkQuantity(orderDto.getProductId(),orderDto.getQuantity()).getBody();
            if(check ==null || !check) {
                System.out.println("quantity check fail "+check);
                return false;
            }

            System.out.println("3");
            Orders order =new Orders();

            order.setProductId(orderDto.getProductId());
            order.setQuantity(orderDto.getQuantity());
            order.setTotalProductPrice(orderDto.getQuantity()*productDto.getProductPrice());
            order.setStatus("PLACED");
            System.out.println("3");
            orderRepository.save(order);

            System.out.println("4");
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Transactional
    public Boolean cancelOrder(int orderId) {
        try {

            Orders order = orderRepository.findById(orderId).orElse(null);

            if (order == null) {
                System.out.println("Order not found");
                return false;
            }

            if (order.getStatus().equals("CANCELLED")) {
                System.out.println("Order already cancelled");
                return false;
            }

            Boolean restored = inventoryFeignClient
                    .restoreQuantity(order.getProductId(), order.getQuantity())
                    .getBody();

            if (restored == null || !restored) {
                System.out.println("Inventory restore failed");
                return false;
            }


            order.setStatus("CANCELLED");
            orderRepository.save(order);

            System.out.println("Order cancelled successfully");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}