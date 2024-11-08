package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<Floor, Integer> {
    boolean existsByFloorName(String floorName);
}