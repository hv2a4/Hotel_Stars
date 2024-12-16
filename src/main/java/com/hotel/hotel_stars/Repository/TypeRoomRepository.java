package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.TypeRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {

//    @Query(value = """
//            SELECT
//                r.id AS roomId,
//                r.room_name,
//                tr.id AS typeroomId,
//                tr.type_room_name,
//                tr.price,
//                tr.acreage,
//                tr.guest_limit,
//                GROUP_CONCAT(DISTINCT CONCAT(atr.amenities_type_room_name) SEPARATOR ', ') AS amenitiesTypeRoomDetails,
//                -- Estimate cost without considering discounts
//                (TIMESTAMPDIFF(DAY, :startDate, :endDate) * tr.price) AS estCost,
//                GROUP_CONCAT(DISTINCT tpi.image_name) AS image_name,
//                tr.describes,
//                type_bed.bed_name
//            FROM
//                type_room tr
//            JOIN
//                room r ON tr.id = r.type_room_id
//            LEFT JOIN
//                booking_room br ON br.room_id = r.id
//            LEFT JOIN
//                booking b ON br.booking_id = b.id
//                AND (
//                    :startDate <= DATE(b.end_at)
//                    AND :endDate >= DATE(b.start_at)
//                )
//            JOIN
//                type_room_amenities_type_room trat ON tr.id = trat.type_room_id
//            JOIN
//                amenities_type_room atr ON trat.amenities_type_room_id = atr.id
//            JOIN
//                type_room_image tpi ON tpi.type_room_id = tr.id
//            JOIN
//                type_bed on tr.type_bed_id = type_bed.id
//            WHERE
//                NOT EXISTS (
//                    SELECT 1
//                    FROM booking_room br_inner
//                    JOIN booking b_inner ON br_inner.booking_id = b_inner.id
//                    WHERE br_inner.room_id = r.id
//                    AND (
//                        DATE(b_inner.start_at) <= :endDate
//                        AND DATE(b_inner.end_at) >= :startDate
//                    )
//                )
//                AND tr.guest_limit <= :guestLimit
//            GROUP BY
//                tr.id, tr.type_room_name, tr.price, tr.acreage, tr.guest_limit, r.room_name, r.id, tr.describes
//            """, nativeQuery = true)
//    List<Object[]> findAvailableRooms(
//            @Param("startDate") Instant startDate,
//            @Param("endDate") Instant endDate,
//            @Param("guestLimit") Integer guestLimit);


    @Query(value = """
            SELECT
                GROUP_CONCAT(DISTINCT r.id ORDER BY r.id) AS roomId,
                GROUP_CONCAT(DISTINCT r.room_name ORDER BY r.room_name) AS roomNames,
                tr.id AS typeroomId,
                tr.type_room_name,
                tr.price,
                tr.acreage,
                tr.guest_limit,
                GROUP_CONCAT(DISTINCT CONCAT(atr.amenities_type_room_name) SEPARATOR ', ') AS amenitiesTypeRoomDetails,
                (TIMESTAMPDIFF(DAY, :startDate, :endDate) * tr.price) AS estCost,
                GROUP_CONCAT(DISTINCT tpi.image_name) AS image_name,
                tr.describes,
                type_bed.bed_name
            FROM
                type_room tr
            JOIN
                room r ON tr.id = r.type_room_id
            LEFT JOIN
                booking_room br ON br.room_id = r.id
            LEFT JOIN
                booking b ON br.booking_id = b.id
                AND (
                    :startDate <= DATE(b.end_at)
                    AND :endDate >= DATE(b.start_at)
                )
            JOIN
                type_room_amenities_type_room trat ON tr.id = trat.type_room_id
            JOIN
                amenities_type_room atr ON trat.amenities_type_room_id = atr.id
            JOIN
                type_room_image tpi ON tpi.type_room_id = tr.id
            JOIN
                type_bed ON tr.type_bed_id = type_bed.id
            WHERE
                NOT EXISTS (
                    SELECT 1
                    FROM booking_room br_inner
                    JOIN booking b_inner ON br_inner.booking_id = b_inner.id
                    WHERE br_inner.room_id = r.id
                    AND (
                        DATE(b_inner.start_at) <= :endDate
                        AND DATE(b_inner.end_at) >= :startDate
                    )  AND b_inner.status_id NOT IN (1, 6)
                )AND (:typeRoomID IS NULL OR tr.id = :typeRoomID)
            GROUP BY
                tr.id
            ORDER BY
                (CASE
                    WHEN tr.guest_limit = :guestLimit THEN 1  
                    WHEN tr.guest_limit > :guestLimit THEN 2  
                    WHEN tr.guest_limit < :guestLimit THEN 3  
                END),
                (CASE
                    WHEN tr.guest_limit > :guestLimit THEN tr.guest_limit
                    WHEN tr.guest_limit < :guestLimit THEN -tr.guest_limit
                    ELSE 0                                      
                END)
            """,
            countQuery = """
            SELECT COUNT(DISTINCT tr.id)
            FROM
                type_room tr
            JOIN
                room r ON tr.id = r.type_room_id
            LEFT JOIN
                booking_room br ON br.room_id = r.id
            LEFT JOIN
                booking b ON br.booking_id = b.id
                AND (
                    :startDate <= DATE(b.end_at)
                    AND :endDate >= DATE(b.start_at)
                )
            WHERE
                NOT EXISTS (
                    SELECT 1
                    FROM booking_room br_inner
                    JOIN booking b_inner ON br_inner.booking_id = b_inner.id
                    WHERE br_inner.room_id = r.id
                    AND (
                        DATE(b_inner.start_at) <= :endDate
                        AND DATE(b_inner.end_at) >= :startDate
                    )  AND b_inner.status_id NOT IN (1, 6)
                )
            """,
            nativeQuery = true)
    Page<Object[]> findAvailableRoomsWithPagination(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("guestLimit") Integer guestLimit,
            @Param("typeRoomID") Integer typeRoomID,
            Pageable pageable);


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
            "    ) " + // Removed the `+` sign here
            "    AND b_inner.status_id NOT IN (1, 6) " +
            ")", nativeQuery = true)
    Long countAvailableRoom(@Param("roomId") Integer roomId,
                            @Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate);
}