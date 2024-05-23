package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_money", nullable = false)
    private Double totalMoney;
}
