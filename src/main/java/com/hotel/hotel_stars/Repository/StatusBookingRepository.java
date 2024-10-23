package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.StatusBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusBookingRepository extends JpaRepository<StatusBooking, Integer> {
}