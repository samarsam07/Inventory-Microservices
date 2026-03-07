package com.samar.order_service.mapper;

import com.samar.order_service.dto.OrderDto;
import com.samar.order_service.model.Orders;

public class OrderMapper {
    public OrderDto orderToDto(Orders order){
        OrderDto orderDto=new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setStatus(order.getStatus());
        orderDto.setProductId(order.getProductId());
        orderDto.setTotalProductPrice(order.getTotalProductPrice());
        return orderDto;
    }

    public Orders orderDtoToOrder(OrderDto orderDto){
        Orders order=new Orders();
        return order;
    }

}
