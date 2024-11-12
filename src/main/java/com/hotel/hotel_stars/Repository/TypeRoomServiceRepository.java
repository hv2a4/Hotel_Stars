package com.hotel.hotel_stars.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotel_stars.Entity.TypeRoomServicePackage;
import com.hotel.hotel_stars.Entity.TypeServiceRoom;

public interface TypeRoomServiceRepository extends JpaRepository<TypeServiceRoom, Integer> {

}
