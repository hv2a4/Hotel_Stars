package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.TypeRoomAmenitiesTypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRoomAmenitiesTypeRoomRepository extends JpaRepository<TypeRoomAmenitiesTypeRoom, Integer> {
    List<TypeRoomAmenitiesTypeRoom> findByTypeRoomId(Integer typeRoomId);
    List<TypeRoomAmenitiesTypeRoom> findByTypeRoom_Id(Integer typeRoomId);
}