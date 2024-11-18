package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
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
                SELECT 
                    tr.id AS typeRoomId,
                    tr.type_room_name AS typeRoomName,
                    tr.price,
                    tr.bed_count AS bedCount,
                    tr.acreage,
                    tr.guest_limit AS guestLimit,
                    tr.describes,
                    MIN(tri.id) AS imageId, -- Lấy giá trị nhỏ nhất của tri.id
                    MIN(tatr.id) AS amenitiesId, -- Lấy giá trị nhỏ nhất của tatr.id
                    COUNT(DISTINCT f.id) AS totalReviews,
                    AVG(f.stars) AS averageStars
                FROM 
                    feedback f
                JOIN 
                    invoice i ON f.invoice_id = i.id
                JOIN 
                    booking b ON i.booking_id = b.id
                JOIN 
                    booking_room br ON b.id = br.booking_id
                JOIN 
                    room r ON br.room_id = r.id
                JOIN 
                    type_room tr ON r.type_room_id = tr.id
                JOIN 
                    type_room_image tri ON tr.id = tri.type_room_id
                JOIN 
                    type_room_amenities_type_room tatr ON tr.id = tatr.type_room_id
                WHERE 
                    f.stars >= 4
                GROUP BY 
                    tr.id, tr.type_room_name, tr.price, tr.bed_count, tr.acreage, tr.guest_limit, tr.describes
                ORDER BY 
                    totalReviews DESC, averageStars DESC
                LIMIT 3
            """, nativeQuery = true)
    List<Object[]> findTop3TypeRoomsWithGoodReviews();


    @Query("SELECT tr FROM TypeRoom tr WHERE tr.typeRoomName LIKE %:keyword%")
    List<TypeRoom> findByTypeRoomNameContaining(@Param("keyword") String keyword);

    @Query(value = """
            SELECT 
                type_room.id AS typeRoomId,
                type_room.type_room_name AS typeRoomName,
                type_room.price AS price,
                type_room.bed_count AS bedCount,
                type_room.acreage AS acreage,
                type_room.guest_limit AS guestLimit,
                type_room.describes AS describes,
                type_bed.bed_name AS bedName,
                GROUP_CONCAT(DISTINCT type_room_image.image_name) AS imageList,
                GROUP_CONCAT(DISTINCT type_room_amenities_type_room.amenities_type_room_id) AS amenitiesList,
                GROUP_CONCAT(DISTINCT feedback.id) AS FeedBack,
                AVG(feedback.stars) average_feedBack,
                accounts.fullname,
                accounts.avatar
            FROM 
                type_room 
                JOIN type_bed ON type_room.type_bed_id = type_bed.id
                JOIN type_room_image ON type_room.id = type_room_image.type_room_id
                JOIN type_room_amenities_type_room ON type_room.id = type_room_amenities_type_room.type_room_id
                JOIN room ON type_room.id = room.type_room_id
                JOIN booking_room ON room.id = booking_room.room_id
                JOIN booking ON booking_room.booking_id = booking.id
                JOIN invoice ON booking.id = invoice.booking_id
                JOIN feedback ON invoice.id = feedback.invoice_id
                JOIN accounts on booking.account_id = accounts.id
            WHERE 
                type_room.id = ?1  and feedback.stars >= 4
            GROUP BY
                type_room.id,
                type_room.type_room_name,
                type_room.price,
                type_room.bed_count,
                type_room.acreage,
                type_room.guest_limit,
                type_room.describes,
                type_bed.bed_name,
                accounts.fullname,
                accounts.avatar 
            """, nativeQuery = true)
    List<Object[]> findTypeRoomDetailsById(Integer roomId);

    Optional<TypeRoom> findByTypeRoomName(String typeRoomName);

}