package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRoomRepository extends JpaRepository<BookingRoom, Integer> {
}