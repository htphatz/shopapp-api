package com.example.shopapp.controllers;

import com.example.shopapp.dtos.MonthlyRevenueDTO;
import com.example.shopapp.entities.RevenueByCategory;
import com.example.shopapp.entities.RevenueByProduct;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.RevenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/revenue")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping("/total-revenue")
    public ResponseEntity<ResponseCustom<?>> getTotalRevenue(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        LocalDateTime localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        double totalRevenue = revenueService.getTotalRevenue(localStartDate, localEndDate);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Get total revenue successfully", totalRevenue));
    }

    @GetMapping("/by-month")
    public ResponseEntity<ResponseCustom<?>> getTotalRevenueByMonth(@Valid @RequestParam int year) {
        List<MonthlyRevenueDTO> listMonthlyRevenue = revenueService.getTotalRevenueByMonth(year);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Get total revenue per month of " + year + " successfully", listMonthlyRevenue));
    }

    @GetMapping("/revenue-by-product")
    public ResponseEntity<ResponseCustom<?>> getRevenueByProduct() {
        List<RevenueByProduct> revenueByProducts = revenueService.getRevenueByProduct();
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Get revenue by product successfully", revenueByProducts));
    }

    @GetMapping("/revenue-by-category")
    public ResponseEntity<ResponseCustom<?>> getRevenueByCategory() {
        List<RevenueByCategory> revenueByCategories = revenueService.getRevenueByCategory();
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Get revenue by category successfully", revenueByCategories));
    }
}
