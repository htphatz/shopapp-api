package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.models.OrderItem;

import java.util.List;

public interface IOrderItemService {
    OrderItem createOrderDetail(OrderItemDTO orderItemDTO);
    OrderItem getOrderDetail(long id);
    OrderItem updateOrderDetail(long id, OrderItemDTO orderItemDTO);
    void deleteById(long id);
    List<OrderItem> findByOrderId(long orderID);
}
