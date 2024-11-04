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
            SELECT COUNT(booking_room.booking_id) AS booking_count,
                   type_room.id AS id,
                   type_room.type_room_name AS typeRoomName,
                   type_room.price AS price,
                   type_room.bed_count AS bedCount,
                   type_room.acreage AS acreage,
                   type_room.guest_limit AS guestLimit,
                   type_room.type_bed_id AS typeBedId
            FROM room
                     JOIN type_room ON room.type_room_id = type_room.id
                     JOIN booking_room br ON room.id = br.room_id
                     JOIN booking ON br.booking_id = booking.id
                     JOIN booking_room ON br.booking_id = booking_room.booking_id
            GROUP BY type_room.id, type_room.type_room_name, type_room.price, type_room.bed_count, 
                     type_room.acreage, type_room.guest_limit, type_room.type_bed_id
            ORDER BY booking_count DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Object[]> findTop3TypeRooms();

}