package com.example.shopapp.repositories;

import com.example.shopapp.entities.OTPReset;
import com.example.shopapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPResetRepository extends JpaRepository<OTPReset, Long> {
    @Query("SELECT or FROM OTPReset or WHERE or.otp = ?1 AND or.user = ?2")
    Optional<OTPReset> findByOtpAndUser(Integer otp, User user);
}
