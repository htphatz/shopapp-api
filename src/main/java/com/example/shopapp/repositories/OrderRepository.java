package com.example.shopapp.repositories;

import com.example.shopapp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") String status);

    @Query("SELECT SUM(o.totalMoney) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status != 'cancelled'")
    double findAllByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT m.month, COALESCE(SUM(o.total_money), 0) AS total_revenue " +
            "FROM ( " +
            "    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
            "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 " +
            "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            ") AS m " +
            "LEFT JOIN orders o ON m.month = MONTH(o.created_at) " +
            "                   AND YEAR(o.created_at) = :year " +
            "                   AND o.status != 'cancelled' " +
            "GROUP BY m.month " +
            "ORDER BY m.month",
            nativeQuery = true)
    List<Object[]> findTotalRevenueByMonth(@Param("year") int year);
}
