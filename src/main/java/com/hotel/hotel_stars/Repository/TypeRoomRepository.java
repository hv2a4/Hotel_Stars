package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.TypeRoomBookingCountDto;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {

    @Query(value = """
        SELECT 
            r.id AS roomId, 
            r.room_name AS roomName, 
            tr.id AS typeRoomId, 
            tr.type_room_name AS typeRoomName, 
            tr.price, 
            (CASE
                    WHEN ds.id IS NULL OR NOT (NOW() BETWEEN ds.start_date AND ds.end_date)\s
                    THEN 0
                    ELSE (tr.price * (1 - IFNULL(ds.percent, 0) / 100))
                 END) AS finalPrice,
            tr.acreage, 
            tr.guest_limit AS guestLimit, 
            GROUP_CONCAT(DISTINCT CONCAT(atr.amenities_type_room_name) SEPARATOR ', ') AS amenitiesTypeRoomDetails, 
             (TIMESTAMPDIFF(DAY, "2024-11-01", "2024-11-03") *\s
                 (CASE
                    WHEN ds.id IS NULL OR NOT (NOW() BETWEEN ds.start_date AND ds.end_date)\s
                    THEN tr.price
                    ELSE (tr.price * (1 - IFNULL(ds.percent, 0) / 100))
                  END)) AS estCost, 
            GROUP_CONCAT(DISTINCT tpi.image_name) AS imageName, tr.describes, ds.percent,ds.discount_name
        FROM 
            type_room tr
        LEFT JOIN
            discount ds ON tr.id = ds.type_room_id
        JOIN 
            room r ON tr.id = r.type_room_id
        LEFT JOIN 
            booking_room br ON br.room_id = r.id
        LEFT JOIN 
            booking b ON br.booking_id = b.id 
            AND (:startDate <=DATE(b.end_at) AND :endDate >=DATE(b.start_at)) 
        JOIN 
            type_room_amenities_type_room trat ON tr.id = trat.type_room_id 
        JOIN 
            amenities_type_room atr ON trat.amenities_type_room_id = atr.id 
        JOIN 
            type_room_image tpi ON tpi.type_room_id = tr.id 
        WHERE 
            b.id IS NULL  
            AND tr.guest_limit = :guestLimit
        GROUP BY 
            tr.id, tr.type_room_name, tr.price, tr.acreage, tr.guest_limit, r.room_name, r.id,tr.describes ,ds.percent,ds.discount_name,ds.id
    """, nativeQuery = true)
    List<Object[]> findAvailableRooms(Instant startDate, Instant endDate, Integer guestLimit);

    // kiểm tên loại phòng có tồn tại trong csdl
    boolean existsByTypeRoomName(String typeRoomName);

    @Query(value = """
            SELECT tr.*
            FROM type_room tr
            JOIN room r ON r.type_room_id = tr.id
            JOIN booking_room br ON r.id = br.room_id
            JOIN booking b ON br.booking_id = b.id
            JOIN invoice i ON b.id = i.booking_id
            JOIN feedback f ON i.id = f.invoice_id
            GROUP BY tr.id
            ORDER BY COUNT(br.booking_id) DESC
            LIMIT 3
            """, nativeQuery = true)
    List<TypeRoom> findTop3TypeRooms();


    @Query("SELECT tr FROM TypeRoom tr WHERE tr.typeRoomName LIKE %:keyword%")
    List<TypeRoom> findByTypeRoomNameContaining(@Param("keyword") String keyword);


    @Query(value = "SELECT COUNT(DISTINCT r_inner.id) as totalRoom " +
            "FROM room r_inner " +
            "WHERE r_inner.id = :roomId " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM booking_room br_inner " +
            "    JOIN booking b_inner " +
            "        ON br_inner.booking_id = b_inner.id " +
            "    WHERE br_inner.room_id = r_inner.id " +
            "    AND ( " +
            "        DATE(b_inner.start_at) <= :endDate " +
            "        AND DATE(b_inner.end_at) >= :startDate " +
            "    ) " +
            ")",
            nativeQuery = true)
    Long  countAvailableRoom(@Param("roomId") Integer roomId,
                           @Param("startDate") Instant startDate,
                           @Param("endDate") Instant endDate);


}