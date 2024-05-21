package com.example.shopapp.controllers;

import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.ok(new ResponseCustom(HttpStatus.BAD_REQUEST.value(),
                        "Create order detail failed"));
            }
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(new ResponseCustom(HttpStatus.CREATED.value(),
                    "Create order successfully", newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseCustom(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id)
    {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long order_id)
    {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order_id);
            List<OrderDetailResponse> orderDetailResponses = orderDetails
                    .stream()
                    .map(OrderDetailResponse::fromOrderDetail)
                    .toList();
            return ResponseEntity.ok().body(orderDetailResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok(new ResponseCustom(HttpStatus.ACCEPTED.value(),
                "Update order detail with id " + id + " successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id)
    {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok(new ResponseCustom(HttpStatus.NO_CONTENT.value(),
                "Delete order detail with id " + id + " successfully"));
    }
}
