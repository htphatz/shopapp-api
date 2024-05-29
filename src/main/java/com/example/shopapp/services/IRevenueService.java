package com.example.shopapp.services;

import com.example.shopapp.dtos.MonthlyRevenueDTO;
import com.example.shopapp.entities.RevenueByCategory;
import com.example.shopapp.entities.RevenueByProduct;

import java.time.LocalDateTime;
import java.util.List;

public interface IRevenueService {
    double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    List<MonthlyRevenueDTO> getTotalRevenueByMonth(int year);

    List<RevenueByProduct> getRevenueByProduct();

    List<RevenueByCategory> getRevenueByCategory();
}
