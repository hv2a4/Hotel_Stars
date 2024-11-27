package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Entity.TypeServiceRoom;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRoomRepository extends JpaRepository<ServiceRoom, Integer> {
    // kiểm tra tên dịch vụ phòng có tồn tai hay không
    boolean existsByServiceRoomName(String serviceRoomName);
    
    Set<ServiceRoom> findByTypeServiceRoomId_Id(Integer typeServiceRoomId);
    
    //khôi
    @Query("SELECT b.serviceRoom FROM BookingRoomServiceRoom b WHERE b.bookingRoom.id = :bookingRoomId")
    List<ServiceRoom> findServiceRoomsByBookingRoomId(@Param("bookingRoomId") Integer bookingRoomId);
    //khôi
}