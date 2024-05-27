package com.example.shopapp.services;

import com.example.shopapp.entities.RevenueByCategory;
import com.example.shopapp.entities.RevenueByProduct;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IRevenueService {
    double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    List<RevenueByProduct> getRevenueByProduct();

    List<RevenueByCategory> getRevenueByCategory();
}
