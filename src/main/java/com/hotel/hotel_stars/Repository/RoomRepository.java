package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.RoomDetailResponseDTO;
import com.hotel.hotel_stars.DTO.Select.RoomListBooking;
import com.hotel.hotel_stars.DTO.Select.RoomListDTO;
import com.hotel.hotel_stars.Entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.*;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "SELECT " +
            "(SELECT COUNT(*) FROM accounts WHERE role_id = 2) AS count_employees, " +
            "(SELECT COUNT(*) FROM accounts WHERE role_id = 3) AS count_customers, " +
            "(SELECT COUNT(ro.id) FROM floors fl JOIN room ro ON fl.id = ro.floor_id) AS total_room " +
            "FROM accounts " +
            "GROUP BY count_employees, count_customers", nativeQuery = true)
    List<Object[]> getCounts();

    @Query(value = "SELECT type_room.id AS type_room_id, " +
            "type_room.type_room_name, " +
            "COUNT(room.id) AS room_count, " +
            "type_room.price, " +
            "type_bed.id AS type_bed, " +
            "type_room.bed_count, " +
            "type_room.guest_limit, " +
            "type_room.acreage, " +
            "type_room_image.id " +
            "FROM room " +
            "JOIN type_room ON room.type_room_id = type_room.id " +
            "JOIN type_bed ON type_room.type_bed_id = type_bed.id " +
            "JOIN type_room_image ON type_room.id = type_room_image.type_room_id " +
            "GROUP BY type_room.id, type_room.type_room_name, type_room.price, " +
            "type_bed.id, type_room.bed_count, type_room.guest_limit, type_room.acreage, type_room_image.id",
            nativeQuery = true)
    List<Object[]> getRoomTypeData();

    List<Room> findByTypeRoomId(Integer typeRoomId);

    @Query(value = """
                SELECT 
                    r.roomName,
                    tr.typeRoomName,
                    f.floorName,
                    sr.statusRoomName,
                    r.id,
                    tr.id,
                    sr.id
                FROM Room r
                JOIN r.typeRoom tr
                JOIN r.floor f
                JOIN r.statusRoom sr
            """)
    List<Object[]> findAllRoomInfo();

    @Query(value = """
            SELECT 
                room.room_name, 
                type_room.type_room_name, 
                booking_room.check_in, 
                booking_room.check_out, 
                accounts.username AS guest_name, 
                status_room.status_room_name 
            FROM 
                type_room 
                JOIN type_room_image ON type_room.id = type_room_image.type_room_id 
                JOIN room ON type_room.id = room.type_room_id 
                JOIN status_room ON room.status_room_id = status_room.id 
                JOIN booking_room ON room.id = booking_room.room_id 
                JOIN booking ON booking_room.booking_id = booking.id 
                JOIN accounts ON booking.account_id = accounts.id 
            WHERE 
                type_room.id = ?1 
            ORDER BY 
                booking_room.check_in ASC
            """, nativeQuery = true)
    List<Object[]> findBookingsByTypeRoomIdOrderedByCheckIn(int typeRoomId);

    @Query("select r from Room r where r.floor.id = ?1")
    List<Room> findByFloorId(Integer floorId);


    Page<Room> findAll(Pageable pageable);

    @Query(value = """
            SELECT 
                GROUP_CONCAT(DISTINCT r.id ORDER BY r.id ASC) AS room_ids,
                GROUP_CONCAT(DISTINCT r.room_name ORDER BY r.room_name ASC) AS room_names,               
                tr.id AS typeRoomId, 
                tr.type_room_name, 
                tr.price, 
                tr.acreage, 
                tr.guest_limit, 
                GROUP_CONCAT(DISTINCT atr.amenities_type_room_name SEPARATOR ', ') AS amenitiesTypeRoomDetails, 
                GROUP_CONCAT(DISTINCT tpi.image_name) AS image_list, 
                tr.describes, 
                GROUP_CONCAT(DISTINCT type_bed.bed_name) AS bed_name, 
                GROUP_CONCAT(DISTINCT trat.amenities_type_room_id) AS amenities_list, 
                (CASE 
                    WHEN discount.id IS NULL OR NOT (NOW() BETWEEN discount.start_date AND discount.end_date) 
                    THEN 0 
                    ELSE (tr.price * (1 - IFNULL(discount.percent, 0) / 100)) 
                END) AS finalPrice, 
                    (TIMESTAMPDIFF(DAY, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY)) * tr.price) AS estCost,
                discount.percent 
            FROM 
                type_room tr 
            JOIN 
                room r ON tr.id = r.type_room_id 
            LEFT JOIN 
                type_room_amenities_type_room trat ON tr.id = trat.type_room_id 
            LEFT JOIN 
                amenities_type_room atr ON trat.amenities_type_room_id = atr.id 
            LEFT JOIN 
                type_room_image tpi ON tpi.type_room_id = tr.id 
            LEFT JOIN 
                type_bed ON tr.type_bed_id = type_bed.id 
            LEFT JOIN 
                discount ON tr.id = discount.type_room_id 
            WHERE 
                NOT EXISTS (
                    SELECT 1
                    FROM booking_room br_inner
                    JOIN booking b_inner ON br_inner.booking_id = b_inner.id
                    WHERE 
                        br_inner.room_id = r.id
                        AND (
                            DATE(b_inner.start_at) <= DATE_ADD(CURDATE(), INTERVAL 1 DAY)
                            AND DATE(b_inner.end_at) >= CURDATE()
                        ) AND b_inner.status_id NOT IN (1, 6)
                )
            GROUP BY 
                tr.id, 
                tr.type_room_name,
                tr.price,
                tr.acreage,
                tr.guest_limit,
                tr.describes,
                discount.id;
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT tr.id) 
                    FROM 
                        type_room tr 
                    JOIN 
                        room r ON tr.id = r.type_room_id 
                    WHERE 
                        NOT EXISTS (
                            SELECT 1
                            FROM booking_room br_inner
                            JOIN booking b_inner ON br_inner.booking_id = b_inner.id
                            WHERE 
                                br_inner.room_id = r.id
                                AND (
                                    DATE(b_inner.start_at) <= DATE_ADD(CURDATE(), INTERVAL 1 DAY)
                                    AND DATE(b_inner.end_at) >= CURDATE()
                                ) AND b_inner.status_id NOT IN (1, 6)
                        )
                    """,
            nativeQuery = true)
    Page<Object[]> findAvailableRooms(Pageable pageable);


    @Query(value = "SELECT r.id AS roomId, r.room_name, tr.id AS typeRoomId, tr.type_room_name, tr.price, tr.acreage, tr.guest_limit, "
            + "GROUP_CONCAT(DISTINCT CONCAT(atr.amenities_type_room_name) SEPARATOR ', ') AS amenitiesTypeRoomDetails, "
            + "GROUP_CONCAT(DISTINCT tpi.image_name) AS image_list, tr.describes, "
            + "GROUP_CONCAT(DISTINCT type_bed.bed_name) AS bed_name, "
            + "GROUP_CONCAT(DISTINCT trat.amenities_type_room_id) AS amenities_list, " + "(CASE "
            + "    WHEN discount.id IS NULL OR NOT (NOW() BETWEEN discount.start_date AND discount.end_date) "
            + "    THEN 0 " + "    ELSE (tr.price * (1 - IFNULL(discount.percent, 0) / 100)) " + "END) AS finalPrice, "
            + "(TIMESTAMPDIFF(DAY, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY)) * " + "(CASE "
            + "    WHEN discount.id IS NULL OR NOT (NOW() BETWEEN discount.start_date AND discount.end_date) "
            + "    THEN tr.price " + "    ELSE (tr.price * (1 - IFNULL(discount.percent, 0) / 100)) "
            + "END)) AS estCost, " + "discount.percent " + "FROM type_room tr "
            + "JOIN room r ON tr.id = r.type_room_id " + "LEFT JOIN booking_room br ON br.room_id = r.id "
            + "LEFT JOIN booking b ON br.booking_id = b.id "
            + "AND (CURDATE() <= DATE(b.end_at) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) >= DATE(b.start_at)) "
            + "JOIN type_room_amenities_type_room trat ON tr.id = trat.type_room_id "
            + "JOIN amenities_type_room atr ON trat.amenities_type_room_id = atr.id "
            + "JOIN type_room_image tpi ON tpi.type_room_id = tr.id " + "JOIN type_bed ON tr.type_bed_id = type_bed.id "
            + "JOIN discount ON tr.id = discount.type_room_id " + "WHERE b.id IS NULL "
            + "GROUP BY r.id, r.room_name, tr.id, tr.type_room_name, tr.price, tr.acreage, tr.guest_limit, tr.describes, discount.id", countQuery = "SELECT COUNT(DISTINCT r.id) "
            + "FROM type_room tr " + "JOIN room r ON tr.id = r.type_room_id "
            + "LEFT JOIN booking_room br ON br.room_id = r.id " + "LEFT JOIN booking b ON br.booking_id = b.id "
            + "AND (CURDATE() <= DATE(b.end_at) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) >= DATE(b.start_at)) "
            + "WHERE b.id IS NULL", nativeQuery = true)
    Page<Object[]> findAvailableRoomss(Pageable pageable);

    @Query(value = "SELECT " +
            "room.id AS roomId, " +
            "type_room.id AS typeRoomId, " +
            "type_room.type_room_name AS typeRoomName, " +
            "type_room.price, " +
            "type_room.bed_count AS bedCount, " +
            "type_room.acreage, " +
            "type_room.guest_limit AS guestLimit, " +
            "type_bed.bed_name AS bedName, " +
            "type_room.describes, " +
            "GROUP_CONCAT(DISTINCT type_room_image.id) AS imageNames, " +
            "GROUP_CONCAT(DISTINCT amenities_type_room.amenities_type_room_name) AS amenities, " +
            "(CASE " +
            "    WHEN discount.id IS NULL OR NOT (NOW() BETWEEN discount.start_date AND discount.end_date) " +
            "    THEN 0 " +
            "    ELSE (type_room.price * (1 - IFNULL(discount.percent, 0) / 100)) " +
            "END) AS finalPrice, " +
            "(TIMESTAMPDIFF(DAY, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY)) * " +
            "(CASE " +
            "    WHEN discount.id IS NULL OR NOT (NOW() BETWEEN discount.start_date AND discount.end_date) " +
            "    THEN type_room.price " +
            "    ELSE (type_room.price * (1 - IFNULL(discount.percent, 0) / 100)) " +
            "END)) AS estCost, " +
            "discount.percent " +
            "FROM type_room " +
            "JOIN type_room_image ON type_room.id = type_room_image.type_room_id " +
            "JOIN type_bed ON type_room.type_bed_id = type_bed.id " +
            "JOIN type_room_amenities_type_room ON type_room.id = type_room_amenities_type_room.type_room_id " +
            "JOIN amenities_type_room ON type_room_amenities_type_room.amenities_type_room_id = amenities_type_room.id " +
            "JOIN room ON type_room.id = room.type_room_id " +
            "LEFT JOIN discount ON type_room.id = discount.type_room_id " +
            "WHERE type_room.id = ?1  " +
            "GROUP BY room.id, " +
            "type_room.id, " +
            "type_room.type_room_name, " +
            "type_room.price, " +
            "type_room.bed_count, " +
            "type_room.acreage, " +
            "type_room.guest_limit, " +
            "type_bed.bed_name, " +
            "type_room.describes, " +
            "discount.id, " +
            "discount.percent",
            nativeQuery = true)
    List<Object[]> findRoomDetailsByRoomId(Integer roomId);


    boolean existsByRoomName(String roomName);

    boolean existsByRoomNameAndIdNot(String roomName, Integer id);

    // khôi
    @Query(value = """
                SELECT r
                FROM Room r
                WHERE NOT EXISTS (
                        SELECT 1
                        FROM BookingRoom br
                        JOIN Booking b ON br.booking.id = b.id
                        WHERE br.room.id = r.id
                            AND (:startDate <= b.endAt AND :endDate >= b.startAt)
                    )
                    AND EXISTS (
                        SELECT 1
                        FROM TypeRoom tr
                        WHERE tr.id = r.typeRoom.id AND tr.guestLimit >= :guestLimit
                    )
                ORDER BY r.roomName
            """)
    Page<Room> findAvailableRoomsWithPagination(@Param("startDate") Instant startDate,
                                                @Param("endDate") Instant endDate,
                                                @Param("guestLimit") Integer guestLimit,
                                                Pageable pageable);

    //khôi

    @Query(value = """
            SELECT 
                room.id AS roomId,
                room.room_name AS roomName,
                room.floor_id AS floorId,
                type_room.id AS typeRoomId,
                type_room.type_room_name AS typeRoomName,
                type_room.price AS price,
                type_room.bed_count AS bedCount,
                type_room.acreage AS acreage,
                type_room.guest_limit AS guestLimit,
                type_room.describes AS describes,
                group_concat(distinct type_room_image.image_name) as imageName
            FROM 
                room
            JOIN 
                type_room ON type_room.id = room.type_room_id
                join type_room_image on type_room.id = type_room_image.type_room_id
            WHERE 
                room.id IN (?1)
            GROUP BY
                    room.id, room.room_name, room.floor_id,
                    type_room.id, type_room.type_room_name,
                    type_room.price, type_room.bed_count,
                    type_room.acreage, type_room.guest_limit,
                    type_room.describes
            """, nativeQuery = true)
    List<Object[]> findRoomsDetailsByIds(List<Integer> roomIds);

    @Query("SELECT new com.hotel.hotel_stars.DTO.Select.RoomListDTO(r.id, r.roomName) " +
            "FROM Room r JOIN r.typeRoom t WHERE t.id = ?1")
    List<RoomListDTO> findRoomsByTypeId(Integer typeRoomId);

    @Query(value = "SELECT COUNT(DISTINCT br.room_id) AS occupiedRooms, " +
            "(SELECT COUNT(DISTINCT r.id) FROM room r) AS totalRooms, " +
            "ROUND((COUNT(DISTINCT br.room_id) / (SELECT COUNT(DISTINCT r.id) FROM room r)) * 100, 2) AS usagePercentage " +
            "FROM booking_room br " +
            "JOIN booking ON br.booking_id = booking.id " +
            "WHERE booking.status_id = 7 " +
            "AND (" +
            "    (DATE(br.check_in) < COALESCE(:endDate, CURRENT_DATE) " +
            "     AND DATE(br.check_out) > COALESCE(:startDate, CURRENT_DATE)) " +
            "    OR " +
            "    (br.check_in IS NULL AND br.check_out IS NULL)" +
            ")", nativeQuery = true)
    Object[] findRoomOccupancy(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "SELECT tr.id as typeRoomId, " +
            "tr.type_room_name AS typeRoomName, " +
            "SUM(i.total_amount - IFNULL(i.total_amount * b.discount_percent / 100, 0)) AS revenue, " +
            "COUNT(br.id) AS bookingCount, " +
            "SUM(i.total_amount - IFNULL(i.total_amount * b.discount_percent / 100, 0)) / NULLIF(COUNT(br.id), 0) AS avgRevenuePerBooking, " +
            "AVG(IFNULL(b.discount_percent, 0)) AS avgDiscountPercent, " +
            "(SUM(i.total_amount - IFNULL(i.total_amount * b.discount_percent / 100, 0)) / " +
            "(SELECT SUM(i2.total_amount - IFNULL(i2.total_amount * b2.discount_percent / 100, 0)) " +
            "FROM booking_room br2 " +
            "JOIN booking b2 ON br2.booking_id = b2.id " +
            "JOIN invoice i2 ON b2.id = i2.booking_id " +
            "WHERE " +
            "CASE " +
            "WHEN :filterOption = 1 THEN DATE(b2.create_at) = CURDATE() " +
            "WHEN :filterOption = 2 THEN DATE(b2.create_at) = CURDATE() - INTERVAL 1 DAY " +
            "WHEN :filterOption = 3 THEN DATE(b2.create_at) >= CURDATE() - INTERVAL 7 DAY " +
            "WHEN :filterOption = 4 THEN MONTH(b2.create_at) = MONTH(CURDATE()) AND YEAR(b2.create_at) = YEAR(CURDATE()) " +
            "WHEN :filterOption = 5 THEN MONTH(b2.create_at) = MONTH(CURDATE() - INTERVAL 1 MONTH) " +
            "AND YEAR(b2.create_at) = YEAR(CURDATE() - INTERVAL 1 MONTH) " +
            "END) * 100) AS revenuePercentage " +
            "FROM booking_room br " +
            "JOIN room r ON br.room_id = r.id " +
            "JOIN type_room tr ON r.type_room_id = tr.id " +
            "JOIN booking b ON br.booking_id = b.id " +
            "JOIN invoice i ON b.id = i.booking_id " +
            "WHERE " +
            "CASE " +
            "WHEN :filterOption = 1 THEN DATE(b.create_at) = CURDATE() " +
            "WHEN :filterOption = 2 THEN DATE(b.create_at) = CURDATE() - INTERVAL 1 DAY " +
            "WHEN :filterOption = 3 THEN DATE(b.create_at) >= CURDATE() - INTERVAL 7 DAY " +
            "WHEN :filterOption = 4 THEN MONTH(b.create_at) = MONTH(CURDATE()) AND YEAR(b.create_at) = YEAR(CURDATE()) " +
            "WHEN :filterOption = 5 THEN MONTH(b.create_at) = MONTH(CURDATE() - INTERVAL 1 MONTH) " +
            "AND YEAR(b.create_at) = YEAR(CURDATE() - INTERVAL 1 MONTH) " +
            "END " +
            "GROUP BY tr.type_room_name , tr.id " +
            "ORDER BY revenue DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> getTopRoomRevenue(@Param("filterOption") Integer filterOption);
}