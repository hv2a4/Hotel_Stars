package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}