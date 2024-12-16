package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.StatusRoom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatusRoomRepository extends JpaRepository<StatusRoom, Integer> {
    boolean existsByStatusRoomName(String statusRoomName);
    
    @Query("SELECT sr FROM StatusRoom sr WHERE sr.id <> ?1 and sr.id in (3,5,4)")
    List<StatusRoom> findAllExcludingId(Integer excludedId);

}