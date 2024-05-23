package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderItemDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderItem;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderItemService orderDetailService;

    @PostMapping
    public ResponseCustom<?> createOrderDetail(
            @Valid OrderItemDTO orderItemDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create order detail failed");
            }
            OrderItem newOrderItem = orderDetailService.createOrderDetail(orderItemDTO);
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            orderItems.add(newOrderItem);
            return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create banner successfully", orderItems);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getOrderDetailById(@PathVariable("id") Long id)
    {
        try {
            OrderItem existingOrderItem = orderDetailService.getOrderDetail(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get order detail with id " + id + " successfully", existingOrderItem);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get order detail with id " + id);
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseCustom<?> getOrderDetails(@PathVariable("order_id") Long orderId)
    {
        try {
            List<OrderItem> orderItems = orderDetailService.findByOrderId(orderId);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get order details with order id " + orderId + " successfully", orderItems);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get order details with order id " + orderId);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateOrderDetail(
            @PathVariable("id") Long id,
            OrderItemDTO orderItemDTO) throws DataNotFoundException {
        try {
            orderDetailService.updateOrderDetail(id, orderItemDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update order detail with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot update order detail with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteOrderDetail(@PathVariable("id") Long id)
    {
        orderDetailService.deleteById(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete order detail with id " + id + " successfully");
    }
}
