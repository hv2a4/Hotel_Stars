package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.ServiceRoom;
import com.hotel.hotel_stars.Entity.TypeServiceRoom;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRoomRepository extends JpaRepository<ServiceRoom, Integer> {
    // kiểm tra tên dịch vụ phòng có tồn tai hay không
    boolean existsByServiceRoomName(String serviceRoomName);
    
    Set<ServiceRoom> findByTypeServiceRoomId_Id(Integer typeServiceRoomId);
}