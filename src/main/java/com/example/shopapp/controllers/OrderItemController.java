package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.models.OrderItem;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderDetailService;

    @PostMapping
    public ArrayDataResponse<OrderItem> createOrderDetail(@Valid OrderItemDTO orderItemDTO) {
        OrderItem newOrderItem = orderDetailService.createOrderDetail(orderItemDTO);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create category successfully", List.of(newOrderItem));
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getOrderDetailById(@Valid @PathVariable("id") Long id) {
        OrderItem existingOrderItem = orderDetailService.getOrderDetail(id);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get order detail with id " + id + " successfully", existingOrderItem);
    }

    @GetMapping("/order/{order_id}")
    public ResponseCustom<?> getOrderDetails(@Valid @PathVariable("order_id") Long orderId) {
        List<OrderItem> orderItems = orderDetailService.findByOrderId(orderId);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get order details with order id " + orderId + " successfully", orderItems);
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateOrderDetail(@Valid @PathVariable("id") Long id, @Valid OrderItemDTO orderItemDTO) {
        orderDetailService.updateOrderDetail(id, orderItemDTO);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update order detail with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete order detail with id " + id + " successfully");
    }
}
