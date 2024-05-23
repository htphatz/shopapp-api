package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderItem;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderItemRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User existingUser = userRepository
               .findById(orderDTO.getUserId())
               .orElseThrow(() -> new DataNotFoundException("Cannot find user with id " + orderDTO.getUserId()));
        Order newOrder = Order.builder()
                .user(existingUser)
                .fullName(orderDTO.getFullName())
                .email(orderDTO.getEmail())
                .phone(orderDTO.getPhone())
                .address(orderDTO.getAddress())
                .note(orderDTO.getNote())
                .totalMoney(orderDTO.getTotalMoney())
                .paymentMethod(orderDTO.getPaymentMethod())
                .active(true)
                .build();
        final Order order = orderRepository.save(newOrder);
        List<OrderItemDTO> items = orderDTO.getItems();
        items.forEach(item -> {
            Long productId = item.getProductId();
            productRepository.findById(productId).ifPresent(product -> {
                final double price = product.getPrice();
                final int numberOfProducts = item.getQuantity();
                // totalAmounts
                final double totalMoney = (Double) price * numberOfProducts;
                OrderItem newOrderItem = OrderItem.builder()
                        .order(order)
                        .product(product)
                        .price(price)
                        .quantity(numberOfProducts)
                        .totalMoney(totalMoney)
                        .build();
                orderItemRepository.save(newOrderItem);
            });
        });
        return newOrder;
    }

    @Override
    public Order getOrderById(long id)  {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order with id " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id " + orderDTO.getUserId()));
        existingOrder.setUser(existingUser);
        existingOrder.setFullName(orderDTO.getFullName());
        existingOrder.setEmail(orderDTO.getEmail());
        existingOrder.setPhone(orderDTO.getPhone());
        existingOrder.setAddress(orderDTO.getAddress());
        existingOrder.setNote(orderDTO.getNote());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setTotalMoney(orderDTO.getTotalMoney());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // Xoa mem
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
