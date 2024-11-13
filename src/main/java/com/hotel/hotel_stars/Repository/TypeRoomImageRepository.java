package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.Entity.TypeRoomImage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRoomImageRepository extends JpaRepository<TypeRoomImage, Integer> {
	List<TypeRoomImage> findByTypeRoomId(Integer typeRoomId);
  }
