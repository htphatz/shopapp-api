package com.example.shopapp.repositories;

import com.example.shopapp.models.Order;
import com.example.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
    @Query("SELECT o FROM Order o WHERE o.user = ?1")
    List<Order> findByUser(User user);
}
