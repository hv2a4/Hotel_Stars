package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.ServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceHotelRepository extends JpaRepository<ServiceHotel, Integer> {
}