package com.example.shopapp.services;

import com.example.shopapp.dtos.CartItemDTO;
import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.ChangeOrderInfoRequest;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.*;
import com.example.shopapp.repositories.OrderItemRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User existingUser = userRepository
               .findById(orderDTO.getUserId())
               .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + orderDTO.getUserId()));
        Order newOrder = Order.builder()
                .user(existingUser)
                .fullName(orderDTO.getFullName())
                .email(orderDTO.getEmail())
                .phone(orderDTO.getPhone())
                .address(orderDTO.getAddress())
                .note(orderDTO.getNote())
                .status(OrderStatus.PENDING)
                .totalMoney(orderDTO.getTotalMoney())
                .paymentMethod(orderDTO.getPaymentMethod())
                .build();
        final Order order = orderRepository.save(newOrder);
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderItem newOrderItem = new OrderItem();

            // Lấy thông tin sản phẩm từ CartItemDTO
            Long productId = cartItemDTO.getProductId();
            Double price = cartItemDTO.getPrice();
            int quantity = cartItemDTO.getQuantity();
            Double totalMoney = cartItemDTO.getTotalMoney();

            // Tìm thông tin Product từ Id nhận đươợc ở trên
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find product with id " + productId));

            // Đặt thông tin cho OrderItem
            newOrderItem.setOrder(order);
            newOrderItem.setProduct(product);
            newOrderItem.setPrice(price);
            newOrderItem.setQuantity(quantity);
            newOrderItem.setTotalMoney(totalMoney);

            // Thêm OrderItem vào danh sách
            orderItems.add(newOrderItem);
        }
        // Lưu danh sách OrderItem vào database
        orderItemRepository.saveAll(orderItems);
        orderDTO.setId(order.getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }

    @Override
    public OrderDTO getOrderById(long id)  {
        Order order =  orderRepository.findById(id).orElse(null);
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);
        List<CartItemDTO> cartItemDTOs = new ArrayList<>();
        for (OrderItem orderItem: orderItems) {
            CartItemDTO cartItemDTO = CartItemDTO.fromOrderItem(orderItem);
            cartItemDTOs.add(cartItemDTO);
        }
        orderDTO.setCartItems(cartItemDTOs);
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            OrderDTO orderDTO = OrderDTO.fromOrder(order);
            List<CartItemDTO> cartItemDTOs = new ArrayList<>();
            for (OrderItem orderItem: orderItems) {
                CartItemDTO cartItemDTO = CartItemDTO.fromOrderItem(orderItem);
                cartItemDTOs.add(cartItemDTO);
            }
            orderDTO.setCartItems(cartItemDTOs);
            orderDTOs.add(orderDTO);
        }
        return orderDTOs;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(long id, String status) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found order with id " + id));
        existingOrder.setStatus(status);
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public Order updateOrderInfo(long id, ChangeOrderInfoRequest changeOrderInfoRequest) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found order with id " + id));
        existingOrder.setFullName(changeOrderInfoRequest.getFullName());
        existingOrder.setAddress(changeOrderInfoRequest.getAddress());
        existingOrder.setPhone(changeOrderInfoRequest.getPhone());
        return existingOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // Xoa mem
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderDTO> findByUserId(long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            OrderDTO orderDTO = OrderDTO.fromOrder(order);
            List<CartItemDTO> cartItemDTOs = new ArrayList<>();
            for (OrderItem orderItem: orderItems) {
                CartItemDTO cartItemDTO = CartItemDTO.fromOrderItem(orderItem);
                cartItemDTOs.add(cartItemDTO);
            }
            orderDTO.setCartItems(cartItemDTOs);
            orderDTOs.add(orderDTO);
        }
        return orderDTOs;
    }

    @Override
    public List<OrderDTO> findByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            OrderDTO orderDTO = OrderDTO.fromOrder(order);
            List<CartItemDTO> cartItemDTOs = new ArrayList<>();
            for (OrderItem orderItem: orderItems) {
                CartItemDTO cartItemDTO = CartItemDTO.fromOrderItem(orderItem);
                cartItemDTOs.add(cartItemDTO);
            }
            orderDTO.setCartItems(cartItemDTOs);
            orderDTOs.add(orderDTO);
        }
        return orderDTOs;
    }

}
