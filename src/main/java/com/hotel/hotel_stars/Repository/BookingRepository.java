package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}