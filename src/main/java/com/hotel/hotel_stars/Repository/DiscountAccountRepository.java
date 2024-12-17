package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.DiscountAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiscountAccountRepository extends JpaRepository<DiscountAccount, Integer> {
    @Query(value = "SELECT * FROM discount_account WHERE discount_id = :discountId AND account_id = :accountId", nativeQuery = true)
    DiscountAccount findByDiscountAndAccount(@Param("discountId") Integer discountId, @Param("accountId") Integer accountId);
    
}