package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}