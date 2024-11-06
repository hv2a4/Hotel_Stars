package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.TypeRoomBookingCountDto;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {

    @Query(value = "SELECT tr.type_room_name, tr.price ,tr.acreage, tr.guest_limit, " +
            "GROUP_CONCAT(atr.amenities_type_room_name) AS amenitiesTypeRoomNames, " +
            "(TIMESTAMPDIFF(DAY, :startDate, :endDate) * tr.price) AS estCost ," +
            "MIN(tpi.image_name) AS image_name " +
            "FROM type_room tr " +
            "JOIN type_room_amenities_type_room trat ON tr.id = trat.type_room_id " +
            "JOIN amenities_type_room atr ON trat.amenities_type_room_id = atr.id " +
            "JOIN type_room_image tpi ON tpi.type_room_id = tr.id " +
            "GROUP BY tr.id, tr.type_room_name, tr.acreage, tr.guest_limit",
            nativeQuery = true)
    List<Object[]> findAllTypeRoomDetailsWithCost(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    // kiểm tên loại phòng có tồn tại trong csdl
    boolean existsByTypeRoomName(String typeRoomName);

    @Query(value = """
            SELECT COUNT(br.booking_id) AS booking_count,
                   tr.id AS id,
                   tr.type_room_name AS typeRoomName,
                   tr.price AS price,
                   tr.bed_count AS bedCount,
                   tr.acreage AS acreage,
                   tr.guest_limit AS guestLimit,
                   tr.type_bed_id AS typeBedId,
                   ROUND(AVG(f.stars), 1) AS averageStars,
                   GROUP_CONCAT(atr.amenities_type_room_name) AS amenities,
                   GROUP_CONCAT(atr.icon) AS amenities_icon
            FROM room r
                     JOIN type_room tr ON r.type_room_id = tr.id
                     JOIN booking_room br ON r.id = br.room_id
                     JOIN booking b ON br.booking_id = b.id
                     JOIN Invoice i ON b.id = i.booking_id
                     JOIN Feedback f ON i.id = f.invoice_id
                     JOIN type_room_amenities_type_room trat ON tr.id = trat.type_room_id
                     JOIN amenities_type_room atr ON trat.amenities_type_room_id = atr.id
            GROUP BY tr.id, tr.type_room_name, tr.price, tr.bed_count, 
                     tr.acreage, tr.guest_limit, tr.type_bed_id
            ORDER BY booking_count DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Object[]> findTop3TypeRooms();

    @Query("SELECT tr FROM TypeRoom tr WHERE tr.typeRoomName LIKE %:keyword%")
    List<TypeRoom> findByTypeRoomNameContaining(@Param("keyword") String keyword);
}