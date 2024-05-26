package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.ChangeOrderInfoRequest;
import com.example.shopapp.entities.Order;

import java.util.List;

public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(long id) ;
    List<OrderDTO> getAllOrders();
    Order updateOrderStatus(long id, String status);
    Order updateOrderInfo(long id, ChangeOrderInfoRequest changeOrderInfoRequest);
    void deleteOrder(long id);
    List<OrderDTO> findByUserId(long userId);
    List<OrderDTO> findByStatus(String status);
}
