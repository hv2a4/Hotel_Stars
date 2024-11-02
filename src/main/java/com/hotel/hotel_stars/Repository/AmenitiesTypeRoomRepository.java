package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.AmenitiesTypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenitiesTypeRoomRepository extends JpaRepository<AmenitiesTypeRoom, Integer> {
    boolean existsByAmenitiesTypeRoomName(String amenitiesTypeRoomName);
}