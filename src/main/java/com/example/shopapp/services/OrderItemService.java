package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderItem;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.OrderItemRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderDetail(OrderItemDTO orderItemDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id " + orderItemDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id " + orderItemDTO.getProductId()));
        OrderItem newOrderItem = OrderItem.builder()
                .order(existingOrder)
                .product(existingProduct)
                .price(orderItemDTO.getPrice())
                .quantity(orderItemDTO.getQuantity())
                .build();
        return orderItemRepository.save(newOrderItem);
    }

    @Override
    public OrderItem getOrderDetail(long id) throws DataNotFoundException {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id " + id));
    }

    @Override
    @Transactional
    public OrderItem updateOrderDetail(long id, OrderItemDTO orderItemDTO) throws DataNotFoundException {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id" + id));
        Order existingOrder = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with id " + orderItemDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id " + orderItemDTO.getProductId()));
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
