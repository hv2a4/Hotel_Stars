package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
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
                SELECT tr.id,
                       tr.typeRoomName,
                       tr.price,
                       tr.bedCount,
                       tr.acreage,
                       tr.guestLimit,
                       tr.describes,
                       ti.id,
                       tatr.id,
                       COUNT(f.id) AS totalReviews,
                       AVG(f.stars) AS averageStars
                FROM TypeRoom tr
                JOIN tr.roomList r
                JOIN r.bookingRooms br
                JOIN br.booking b
                JOIN b.invoices i
                JOIN i.feedbackList f
                JOIN tr.typeRoomImages ti
                JOIN tr.typeRoomAmenitiesTypeRoomList tatr
                WHERE f.stars >= 4
                GROUP BY tr.id
                ORDER BY totalReviews DESC, averageStars DESC
            """, nativeQuery = false)
    List<Object[]> findTop3TypeRoomsWithGoodReviews();


    @Query("SELECT tr FROM TypeRoom tr WHERE tr.typeRoomName LIKE %:keyword%")
    List<TypeRoom> findByTypeRoomNameContaining(@Param("keyword") String keyword);

}