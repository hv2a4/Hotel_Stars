package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.DiscountAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscountAccountRepository extends JpaRepository<DiscountAccount, Integer> {
    DiscountAccount findByDiscountAndAccount(Discount discount, Account account);
}