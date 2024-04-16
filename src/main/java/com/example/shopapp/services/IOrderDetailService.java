package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws Exception;
    void deleteById(long id);
    List<OrderDetail> findByOrderId(long orderID);
}
