package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderItem;

import java.util.List;

public interface IOrderItemService {
    OrderItem createOrderDetail(OrderItemDTO orderItemDTO) throws Exception;
    OrderItem getOrderDetail(long id) throws DataNotFoundException;
    OrderItem updateOrderDetail(long id, OrderItemDTO orderItemDTO) throws Exception;
    void deleteById(long id);
    List<OrderItem> findByOrderId(long orderID);
}
