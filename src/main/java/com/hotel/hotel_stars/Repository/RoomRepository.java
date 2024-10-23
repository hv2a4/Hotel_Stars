package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}