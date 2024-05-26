package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.Order;
import com.example.shopapp.entities.OrderItem;
import com.example.shopapp.entities.Product;
import com.example.shopapp.repositories.OrderItemRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderDetail(OrderItemDTO orderItemDTO) {
        Order existingOrder = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find order with id " + orderItemDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find product with id " + orderItemDTO.getProductId()));
        OrderItem newOrderItem = OrderItem.builder()
                .order(existingOrder)
                .product(existingProduct)
                .price(orderItemDTO.getPrice())
                .quantity(orderItemDTO.getQuantity())
                .build();
        return orderItemRepository.save(newOrderItem);
    }

    @Override
    public OrderItem getOrderDetail(long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find OrderDetail with id " + id));
    }

    @Override
    @Transactional
    public OrderItem updateOrderDetail(long id, OrderItemDTO orderItemDTO){
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find OrderDetail with id" + id));
        Order existingOrder = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Order with id " + orderItemDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Product with id " + orderItemDTO.getProductId()));
        existingOrderItem.setOrder(existingOrder);
        existingOrderItem.setProduct(existingProduct);
        existingOrderItem.setPrice(orderItemDTO.getPrice());
        existingOrderItem.setTotalMoney(orderItemDTO.getTotalMoney());
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> findByOrderId(long orderID) {
        return orderItemRepository.findByOrderId(orderID);
    }
}
