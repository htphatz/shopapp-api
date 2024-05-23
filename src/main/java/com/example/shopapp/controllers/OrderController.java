package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseCustom<?> createOrder(
            @RequestBody  OrderDTO orderDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create order failed");
            }
            Order newOrder = orderService.createOrder(orderDTO);
            List<Order> orders = new ArrayList<Order>();
            orders.add(newOrder);
            return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create banner successfully", orders);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("user/{user_id}")
    public ResponseCustom<List<Order>> getOrdersByUserId(@Valid @PathVariable("user_id") Long id)
    {
        try {
            List<Order> orders = orderService.findByUserId(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get all orders with user id " + id + " successfully", orders);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get orders with user id " + id);
        }
    }

    @GetMapping("/me")
    public ResponseCustom<List<Order>> getOrdersForCurrentUser()
    {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<Order> orders = orderService.findByUserId(user.getId());
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all orders of current user successfully", orders);
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String currentUsername = authentication.getName();
//            Optional<User> optionalUser = userRepository.findByEmail(currentUsername);
//            if (optionalUser.isEmpty()) {
//                return ResponseEntity.badRequest().body("Cannot find user with phone: " + currentUsername);
//            } else {
//                User currentUser = optionalUser.get();
//                List<Order> orders = orderService.findByUser(currentUser);
//                return ResponseEntity.ok(orders);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        return null;
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getOrderById(@Valid @PathVariable("id") long id)
    {
        try {
            Order existingOrder = orderService.getOrderById(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get order with id " + id + " successfully", existingOrder);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get order with id " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateOrder(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDTO orderDTO)
    {
        try {
            orderService.updateOrder(id, orderDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update order with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot update order with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteOrder(@Valid @PathVariable("id") Long id)
    {
        // Xoa mem => cap nhat active = 0
        orderService.deleteOrder(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete order with id " + id + " successfully");
    }
}
