package com.example.shopapp.repositories;

import com.example.shopapp.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("SELECT v FROM Voucher v WHERE v.code = ?1 AND v.expirationDate > CURRENT_DATE")
    Optional<Voucher> findByCodeAndNotExpired(String code);
    @Query("SELECT v FROM Voucher v WHERE v.expirationDate > CURRENT_DATE")
    List<Voucher> findAllNotExpired();
}

