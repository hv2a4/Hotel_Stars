package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Discount findByDiscountName(String discountName);

    @Query("SELECT d FROM Discount d WHERE d.typeRoom = :typeRoom")
    List<Discount> findByRoomTypeId(@Param("typeRoom") TypeRoom typeRoom);
    @Query(value = "SELECT * FROM discount WHERE NOW() BETWEEN start_date AND end_date AND type_room_id = :typeRoomId", nativeQuery = true)
    List<Discount> findActiveDiscountsForTypeRoom(@Param("typeRoomId") Integer typeRoomId);

}