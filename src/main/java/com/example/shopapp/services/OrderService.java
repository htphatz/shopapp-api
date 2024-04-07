package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderStatus;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // Tim xem user id ton tai khong
        User user = userRepository
               .findById(orderDTO.getUserId())
               .orElseThrow(() -> new DataNotFoundException("Cannot find user with id " + orderDTO.getUserId()));
        // Convert orderDTO -> order. Dung thu vien ModelMapper;
        // Tao mot luong bang anh xa rieng de kiem soat anh xa
        // Vi OrderDTO khong co truong Id nen se bo qua mapper truong Id
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setShippingMethod(OrderStatus.PENDING);
        // Kiem tra shippingDate
        LocalDate shippingDate = (orderDTO.getShippingDate() == null) ? LocalDate.now() :orderDTO.getShippingDate() ;
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date muse be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(long id)  {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order with id " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id " + orderDTO.getUserId()));
        // Tao mot luong anh xa rieng de kiem soat anh xa
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cap nhat order tu orderDTO
        modelMapper.map(orderDTO, existingOrder);
        existingOrder.setUser(existingUser);
        return orderRepository.save(existingOrder);
    }

    @Override
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
}
