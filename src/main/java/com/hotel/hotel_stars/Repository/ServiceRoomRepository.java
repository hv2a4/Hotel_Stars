package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.ServiceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRoomRepository extends JpaRepository<ServiceRoom, Integer> {
    // kiểm tra tên dịch vụ phòng có tồn tai hay không
    boolean existsByServiceRoomName(String serviceRoomName);
}