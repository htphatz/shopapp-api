package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.ChangeOrderInfoRequest;
import com.example.shopapp.entities.User;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ArrayDataResponse<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
            OrderDTO newOrder = orderService.createOrder(orderDTO);
            return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create order successfully", List.of(newOrder));
    }

    @GetMapping("/user/{user_id}")
    public ResponseCustom<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long id) {
        List<OrderDTO> orders = orderService.findByUserId(id);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all orders with user id " + id + " successfully", orders);
    }

    @GetMapping("/me")
    public ResponseCustom<?> getOrdersOfCurrentUser() {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<OrderDTO> orders = orderService.findByUserId(user.getId());
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all order of me successfully", orders);
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getOrderById(@Valid @PathVariable("id") long id) {
        OrderDTO existingOrder = orderService.getOrderById(id);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get order with id " + id + " successfully", existingOrder);
    }

    @GetMapping
    public ResponseCustom<?> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all order successfully", orders);
    }

    @GetMapping("/status/{status}")
    public ResponseCustom<?> getOrdersByStatus(@Valid @PathVariable("status") String status) {
        List<OrderDTO> orders = orderService.findByStatus(status);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all orders with status " + status + " successfully", orders);
    }

    @PatchMapping("/{id}")
    public ResponseCustom<?> updateOrder(@Valid @PathVariable("id") Long id, @Valid @RequestPart("status") String status) {
        orderService.updateOrderStatus(id, status);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update order with id " + id + " successfully");
    }

    @PatchMapping("/info/{id}")
    public ResponseCustom<?> updateOrderInfo(@Valid @PathVariable("id") Long id, @Valid ChangeOrderInfoRequest changeOrderInfoRequest) {
        orderService.updateOrderInfo(id, changeOrderInfoRequest);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update order's information with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteOrder(@Valid @PathVariable("id") Long id) {
        // Xoa mem => cap nhat status = cancelled
        orderService.deleteOrder(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete order with id " + id + " successfully");
    }
}
