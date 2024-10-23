package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelImageRepository extends JpaRepository<HotelImage, Integer> {
}