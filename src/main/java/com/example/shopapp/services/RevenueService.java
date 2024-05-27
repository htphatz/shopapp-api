package com.example.shopapp.services;

import com.example.shopapp.entities.*;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService implements IRevenueService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    @Override
    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findAllByOrderDateBetween(startDate, endDate);
    }

    @Override
    public List<RevenueByProduct> getRevenueByProduct() {
        List<RevenueByProduct> revenueByProduct = new ArrayList<>();

        // Lấy tất cả sản phẩm và tính doanh thu
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            double productRevenue = product.getSoldQuantity() * product.getPrice();
            revenueByProduct.add(new RevenueByProduct(product, productRevenue));
        }
        return revenueByProduct;
    }

    @Override
    public List<RevenueByCategory> getRevenueByCategory() {
        List<RevenueByCategory> revenueByCategory = new ArrayList<>();

        // Lấy tất cả các danh mục và tính doanh thu
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            double categoryRevenue = 0;
            List<Product> products = productRepository.searchProducts(category.getId(), null);
            // Lấy tất cả sản phẩm trong mỗi doanh mục và tính doanh thu
            for (Product product : products) {
                double productRevenue = product.getSoldQuantity() * product.getPrice();
                categoryRevenue += productRevenue;
            }
            revenueByCategory.add(new RevenueByCategory(category, categoryRevenue));
        }
        return revenueByCategory;
    }
}
