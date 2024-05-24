package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.OrderItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderServiceTest {

//    @CsvFileSource(files="src/test/resources/order-items.csv",numLinesToSkip = 1)
    @Test
    public void testCreateOrder() throws Exception {
        final List<OrderItemDTO> items=List.of(
                new OrderItemDTO(1L,2L,0D,3,0D),
                new OrderItemDTO(1L,1L,0D,2,0D),
                new OrderItemDTO(1L,3L,0D,6,0D)
        );
        final OrderDTO orderDTO = OrderDTO.builder()
                .userId(3L)
                .phone("0898993601")
                .email("vutien.dat.3601@gmail.com")
                .address("Quan 7, TP HCM")
//                .orderItems(items)
                .build();
        orderService.createOrder(orderDTO);
    }
    private OrderService orderService;

    @Autowired
    public OrderServiceTest(OrderService orderService) {
        this.orderService = orderService;
    }

}
