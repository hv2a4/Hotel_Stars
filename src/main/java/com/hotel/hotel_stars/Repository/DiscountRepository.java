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

    @Query(value = "SELECT ds.* " +
            "FROM discount ds " +
            "JOIN discount_account da ON ds.id = da.discount_id " +
            "JOIN accounts ac ON ac.id = da.account_id " +
            "WHERE ac.username = :username " +
            "AND CURDATE() BETWEEN DATE(ds.start_date) AND DATE(ds.end_date) and da.status_da=0",
            nativeQuery = true)
    List<Discount> findDiscountsByUsername(@Param("username") String username);
}