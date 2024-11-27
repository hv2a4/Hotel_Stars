package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.AmenitiesHotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenitiesHotelRepository extends JpaRepository<AmenitiesHotel, Integer> {
    boolean existsByAmenitiesHotelName(String amenitiesHotelName);
}