package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.CustomerReservation;
import com.hotel.hotel_stars.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b.id, tr.typeRoomName, r.roomName, br.checkIn, br.checkOut, "
            + "DATEDIFF(br.checkOut, br.checkIn) AS numberOfDays " + "FROM Account a " + "JOIN a.bookingList b "
            + "JOIN b.bookingRooms br " + "JOIN br.room r " + "JOIN r.typeRoom tr " + "WHERE a.id = :accountId")
    List<Object[]> findBookingDetailsByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT mp.methodPaymentName, b.statusPayment, invoice.totalAmount " + "FROM Booking b "
            + "JOIN b.methodPayment mp " + "JOIN b.account a " + "JOIN Invoice invoice ON b.id = invoice.booking.id "
            + "WHERE a.id = :accountId")
    List<Object[]> findPaymentInfoByAccountId(@Param("accountId") Integer accountId); // Thêm @Param

    @Query(value = "SELECT booking.id, " + "accounts.id, " + "status_booking.id, " + "method_payment.id, "
            + "booking_room.id, " + "room.id, " + "type_room.id, " + "invoice.id, " + "room.room_name, "
            + "method_payment.method_payment_name, " + "status_room.status_room_name, "
            + "status_booking.status_booking_name, " + "booking.create_at, " + "booking.start_at, " + "booking.end_at, "
            + "accounts.fullname, " + "roles.role_name, " + // Sửa lỗi ở đây: thêm dấu phẩy
            "type_room.type_room_name, " + // Không cần dấu phẩy ở cuối dòng này
            "invoice.total_amount, " + "type_room.guest_limit AS max_guests " + "FROM accounts "
            + "JOIN booking ON accounts.id = booking.account_id "
            + "JOIN status_booking ON booking.status_id = status_booking.id "
            + "JOIN method_payment ON booking.method_payment_id = method_payment.id "
            + "JOIN booking_room ON booking.id = booking_room.booking_id "
            + "JOIN room ON booking_room.room_id = room.id " + "JOIN type_room ON room.type_room_id = type_room.id "
            + "JOIN invoice ON booking.id = invoice.booking_id "
            + "JOIN status_room ON room.status_room_id = status_room.id "
            + "JOIN roles ON accounts.role_id = roles.id", nativeQuery = true)
    List<Object[]> findAllBookingDetailsUsingSQL();

    @Query("SELECT DISTINCT new com.hotel.hotel_stars.DTO.Select.CustomerReservation("
            + "a.id, a.fullname, a.phone, a.email, " + "b.id, b.startAt, b.endAt, "
            + "tr.guestLimit, tr.typeRoomName, r.roomName, " + "tr.price, i.totalAmount, sb.statusBookingName, "
            + "mp.methodPaymentName, b.statusPayment, role.roleName) " + "FROM Account a "
            + "JOIN Booking b ON a.id = b.account.id " + "JOIN Invoice i ON b.id = i.booking.id "
            + "JOIN MethodPayment mp ON b.methodPayment.id = mp.id " + "JOIN StatusBooking sb ON b.status.id = sb.id "
            + "JOIN BookingRoom br ON b.id = br.booking.id " + "JOIN Room r ON br.room.id = r.id "
            + "JOIN TypeRoom tr ON r.typeRoom.id = tr.id " + "JOIN Role role ON a.role.id = role.id "
            + "WHERE b.id = :bookingId and r.roomName = :roomName")
    Optional<CustomerReservation> findBookingDetailsById(@Param("bookingId") Integer bookingId,
                                                         @Param("roomName") String roomName);

    @Query(value = """
            SELECT
                room.id AS roomId,
                room.room_name AS roomName,
                type_room.type_room_name AS typeRoomName,
                type_room.guest_limit AS guestLimit,
                type_room.bed_count AS bedCount,
                type_room.acreage AS acreage,
                type_room.describes AS describes,
                type_room_image.id AS imageId,
                status_room.status_room_name AS statusRoomName,
                type_room.id AS typeRoomId,
                type_room.price AS price,
                type_room_amenities_type_room.id AS amenitiesId
            FROM
                room
                JOIN type_room ON room.type_room_id = type_room.id
                JOIN type_room_image ON type_room.id = type_room_image.type_room_id
                JOIN status_room ON room.status_room_id = status_room.id
                JOIN type_room_amenities_type_room ON type_room.id = type_room_amenities_type_room.type_room_id
                JOIN booking_room ON room.id = booking_room.room_id
                JOIN booking ON booking_room.booking_id = booking.id
            WHERE
                status_room.status_room_name = 'phòng trống'
            ORDER BY
                room.room_name
            """, nativeQuery = true)
    List<Object[]> findAvailableRooms();

    List<Booking> findByAccount_Id(Integer accountId);

    @Query(value = """
            SELECT b
            FROM Booking b
            WHERE
                (:startDate IS NOT NULL AND :endDate IS NOT NULL AND DATE(b.startAt) BETWEEN :startDate AND :endDate)
            ORDER BY b.createAt DESC
            """)
    List<Booking> findBookingsByTime(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


	// khoi
	@Query("SELECT b FROM Booking b JOIN b.bookingRooms br WHERE br.room.id = :roomId AND b.status.id IN (2, 4, 7, 10)")
	List<Booking> findBookingsByRoomId(@Param("roomId") Integer roomId);

    @Query(value = """
            SELECT
                DATE(i.create_at) AS bookingDate,
                COUNT(b.id) AS totalBookings,
                COUNT(br.id) AS totalRoomsBooked,
                SUM(i.total_amount) AS totalBookingValue,
                SUM(CASE
                    WHEN b.status_payment = TRUE THEN i.total_amount
                    ELSE 0
                END) AS totalPaid
            FROM
                booking b
            INNER JOIN
                invoice i ON b.id = i.booking_id
            LEFT JOIN
                booking_room br ON b.id = br.booking_id
            WHERE
                i.create_at BETWEEN :startDate AND :endDate
            GROUP BY
                DATE(i.create_at)
            ORDER BY
                bookingDate
            """, nativeQuery = true)
    List<Object[]> getBookingStatisticsByDateRange(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.invoice i " +
            "WHERE DATE(i.createAt) = :date")
    List<Booking> findBookingsByStartAtWithInvoice(@Param("date") LocalDate date);

    // khoi


    @Query(value = """
                WITH AggregatedService AS (
                    SELECT 
                        br.booking_id,
                        GROUP_CONCAT(DISTINCT svr.service_room_name SEPARATOR ', ') AS serviceRoomName,
                        SUM(IFNULL(brsr.quantity * brsr.price, 0)) AS totalsService
                    FROM booking_room_service_room brsr
                    LEFT JOIN service_room svr ON brsr.service_room_id = svr.id
                    JOIN booking_room br ON brsr.booking_room_id = br.id
                    GROUP BY br.booking_id
                ),
                AggregatedRoomDetails AS (
                    SELECT
                        br.booking_id,
                        tr.type_room_name,
                        GROUP_CONCAT(DISTINCT rm.room_name SEPARATOR ', ') AS roomNames,
                        SUM(br.price) AS totalRoom
                    FROM booking_room br
                    JOIN room rm ON br.room_id = rm.id
                    JOIN type_room tr ON rm.type_room_id = tr.id
                    GROUP BY br.booking_id, tr.type_room_name
                ),
                AggregatedImages AS (
                    SELECT 
                        br.booking_id,
                        MIN(tyi.image_name) AS image
                    FROM booking_room br
                    JOIN room rm ON br.room_id = rm.id
                    JOIN type_room tr ON rm.type_room_id = tr.id
                    LEFT JOIN type_room_image tyi ON tr.id = tyi.type_room_id
                    GROUP BY br.booking_id
                ),
                FinalAggregatedRooms AS (
                    SELECT 
                        booking_id,
                        GROUP_CONCAT(
                            CONCAT(type_room_name, ' (', roomNames, ')') 
                            SEPARATOR ', '
                        ) AS roomInfo,
                        SUM(totalRoom) AS totalRoom
                    FROM AggregatedRoomDetails
                    GROUP BY booking_id
                ),
                AggregatedServiceHotel AS (
                    SELECT 
                        br.booking_id,
                        GROUP_CONCAT(DISTINCT svr.service_hotel_name SEPARATOR ', ') AS serviceHotelName,
                        SUM(IFNULL(brsh.quantity * brsh.price, 0)) AS totalsServiceHotel
                    FROM booking_room_service_hotel brsh
                    LEFT JOIN service_hotel svr ON brsh.service_hotel_id = svr.id
                    JOIN booking_room br ON brsh.booking_room_id = br.id
                    GROUP BY br.booking_id
                )
                SELECT  
                    bk.id AS bk_id,
                    CONCAT('BK', DATE_FORMAT(bk.create_at, '%d%m%Y'),'TT', bk.id) AS bkformat,
                    DATE_FORMAT(bk.create_at, '%d/%m/%Y') AS create_at,
                    DATE_FORMAT(bk.start_at, '%d/%m/%Y') AS start_at,
                    DATE_FORMAT(bk.end_at, '%d/%m/%Y') AS end_at,
                    ac.fullname AS fullname,
                    ac.avatar AS avatar,
                    sbk.id AS statusBkID,
                    sbk.status_booking_name AS statusBkName,
                    iv.id AS iv_id,
                    CASE 
                        WHEN bk.discount_percent IS NOT NULL AND bk.discount_name IS NOT NULL THEN 
                            fr.totalRoom * (1 - bk.discount_percent / 100)
                        ELSE 
                            fr.totalRoom
                    END AS totalRoom,
                    fb.id AS fb_id,
                    fb.content AS content,
                    fb.stars AS stars,
                    fr.roomInfo AS roomInfo,
                    ai.image AS image, 
                    CASE 
                        WHEN ags.serviceRoomName IS NULL AND arsh.serviceHotelName IS NULL THEN NULL
                        ELSE CONCAT(
                            IFNULL(ags.serviceRoomName, ''), 
                            IF(ags.serviceRoomName IS NOT NULL AND arsh.serviceHotelName IS NOT NULL, ', ', ''), 
                            IFNULL(arsh.serviceHotelName, '')
                        )
                    END AS combinedServiceNames,
                    CASE 
                        WHEN ags.totalsService IS NULL AND arsh.totalsServiceHotel IS NULL THEN NULL
                        ELSE IFNULL(ags.totalsService, 0) + IFNULL(arsh.totalsServiceHotel, 0)
                    END AS combinedTotalServices,
                    CASE
                        WHEN bk.discount_percent IS NOT NULL AND bk.discount_name IS NOT NULL THEN
                            fr.totalRoom * (1 - bk.discount_percent / 100)
                            + IFNULL(ags.totalsService, 0)
                            + IFNULL(arsh.totalsServiceHotel, 0)
                        ELSE
                            fr.totalRoom
                            + IFNULL(ags.totalsService, 0)
                            + IFNULL(arsh.totalsServiceHotel, 0)
                    END AS totalBooking, 
                    bk.discount_percent AS discountPercent, 
                    bk.discount_name AS discountName
                FROM booking bk
                JOIN status_booking sbk ON sbk.id = bk.status_id
                JOIN accounts ac ON ac.id = bk.account_id
                LEFT JOIN invoice iv ON bk.id = iv.booking_id
                LEFT JOIN feedback fb ON iv.id = fb.invoice_id  
                LEFT JOIN AggregatedService ags ON bk.id = ags.booking_id
                LEFT JOIN FinalAggregatedRooms fr ON bk.id = fr.booking_id
                LEFT JOIN AggregatedImages ai ON bk.id = ai.booking_id
                LEFT JOIN AggregatedServiceHotel arsh ON bk.id = arsh.booking_id
                WHERE bk.account_id = :accountId AND bk.status_id != 1
                GROUP BY 
                    bk.id,
                    ac.fullname,
                    ac.avatar,
                    sbk.id,
                    sbk.status_booking_name,
                    iv.id,
                    fb.id,
                    fb.content,
                    fb.stars,
                    fr.roomInfo,
                    ai.image,
                    fr.totalRoom,
                    ags.serviceRoomName,
                    arsh.serviceHotelName,
                    ags.totalsService,
                    bk.discount_percent,
                    bk.discount_name,
                    arsh.totalsServiceHotel
                ORDER BY bk.id DESC
            """, nativeQuery = true)
    List<Object[]> findBookingsByAccountId(@Param("accountId") Integer accountId);

    @Query(value = "SELECT COUNT(DISTINCT br.room_id) AS occupiedRooms, " +
            "(SELECT COUNT(DISTINCT r.id) FROM room r) AS totalRooms, " +
            "ROUND((COUNT(DISTINCT br.room_id) / (SELECT COUNT(DISTINCT r.id) FROM room r)) * 100, 2) AS usagePercentage " +
            "FROM booking_room br " +
            "JOIN booking ON br.booking_id = booking.id " +
            "WHERE booking.status_id = 4 " +
            "AND (" +
            "    (br.check_in < :endDate AND br.check_out > :startDate) " +
            "    OR " +
            "    (br.check_in IS NULL AND br.check_out IS NULL)" +
            ")", nativeQuery = true)
    Object[] findRoomUsage(@Param("startDate") String startDate, @Param("endDate") String endDate);


	@Query(value = """
    WITH AggregatedService AS (
        SELECT 
            br.booking_id,
            GROUP_CONCAT(DISTINCT svr.service_room_name SEPARATOR ', ') AS serviceRoomName,
            SUM(IFNULL(brsr.quantity * brsr.price, 0)) AS totalsService
        FROM booking_room_service_room brsr
        LEFT JOIN service_room svr ON brsr.service_room_id = svr.id
        JOIN booking_room br ON brsr.booking_room_id = br.id
        GROUP BY br.booking_id
    ),
    AggregatedRoomDetails AS (
        SELECT
            br.booking_id,
            SUM(br.price) AS totalRoom
        FROM booking_room br
        JOIN room rm ON br.room_id = rm.id
        JOIN type_room tr ON rm.type_room_id = tr.id
        GROUP BY br.booking_id, tr.type_room_name
    ),
    AggregatedImages AS (
        SELECT 
            br.booking_id,
            MIN(tyi.image_name) AS image
        FROM booking_room br
        JOIN room rm ON br.room_id = rm.id
        JOIN type_room tr ON rm.type_room_id = tr.id
        LEFT JOIN type_room_image tyi ON tr.id = tyi.type_room_id
        GROUP BY br.booking_id
    ),
    FinalAggregatedRooms AS (
        SELECT 
            booking_id,
            GROUP_CONCAT(
                CONCAT(type_room_name, ' (', roomNames, ')') 
                SEPARATOR ', '
            ) AS roomInfo,
            SUM(totalRoom) AS totalRoom
        FROM AggregatedRoomDetails
        GROUP BY booking_id
    ),
    AggregatedServiceHotel AS (
        SELECT 
            br.booking_id,
            GROUP_CONCAT(DISTINCT svr.service_hotel_name SEPARATOR ', ') AS serviceHotelName,
            SUM(IFNULL(brsh.quantity * brsh.price, 0)) AS totalsServiceHotel
        FROM booking_room_service_hotel brsh
        LEFT JOIN service_hotel svr ON brsh.service_hotel_id = svr.id
        JOIN booking_room br ON brsh.booking_room_id = br.id
        GROUP BY br.booking_id
    )
    SELECT  
        bk.id AS bk_id,
        CONCAT('BK', DATE_FORMAT(bk.create_at, '%d%m%Y'),'TT', bk.id) AS bkformat,
        DATE_FORMAT(bk.create_at, '%d/%m/%Y %H:%i:%s') AS create_at,
        DATE_FORMAT(bk.start_at, '%d/%m/%Y') AS start_at,
        DATE_FORMAT(bk.end_at, '%d/%m/%Y') AS end_at,
        ac.fullname AS fullname,
        ac.avatar AS avatar,
        sbk.id AS statusBkID,
        sbk.status_booking_name AS statusBkName,
        iv.id AS iv_id,
        CASE 
            WHEN bk.discount_percent IS NOT NULL AND bk.discount_name IS NOT NULL THEN 
                fr.totalRoom * (1 - bk.discount_percent / 100)
            ELSE 
                fr.totalRoom
        END AS totalRoom,
        fb.id AS fb_id,
        fb.content AS content,
        fb.stars AS stars,
        fr.roomInfo AS roomInfo,
        ai.image AS image, 
        CASE 
            WHEN ags.serviceRoomName IS NULL AND arsh.serviceHotelName IS NULL THEN NULL
            ELSE CONCAT(
                IFNULL(ags.serviceRoomName, ''), 
                IF(ags.serviceRoomName IS NOT NULL AND arsh.serviceHotelName IS NOT NULL, ', ', ''), 
                IFNULL(arsh.serviceHotelName, '')
            )
        END AS combinedServiceNames,
        CASE 
            WHEN ags.totalsService IS NULL AND arsh.totalsServiceHotel IS NULL THEN NULL
            ELSE IFNULL(ags.totalsService, 0) + IFNULL(arsh.totalsServiceHotel, 0)
        END AS combinedTotalServices,
        CASE
            WHEN bk.discount_percent IS NOT NULL AND bk.discount_name IS NOT NULL THEN
                fr.totalRoom * (1 - bk.discount_percent / 100)
                + IFNULL(ags.totalsService, 0)
                + IFNULL(arsh.totalsServiceHotel, 0)
            ELSE
                fr.totalRoom
                + IFNULL(ags.totalsService, 0)
                + IFNULL(arsh.totalsServiceHotel, 0)
        END AS totalBooking, 
        bk.discount_percent AS discountPercent, 
        bk.discount_name AS discountName
    FROM booking bk
    JOIN status_booking sbk ON sbk.id = bk.status_id
    JOIN accounts ac ON ac.id = bk.account_id
    LEFT JOIN invoice iv ON bk.id = iv.booking_id
    LEFT JOIN feedback fb ON iv.id = fb.invoice_id  
    LEFT JOIN AggregatedService ags ON bk.id = ags.booking_id
    LEFT JOIN FinalAggregatedRooms fr ON bk.id = fr.booking_id
    LEFT JOIN AggregatedImages ai ON bk.id = ai.booking_id
    LEFT JOIN AggregatedServiceHotel arsh ON bk.id = arsh.booking_id
    WHERE bk.account_id = :accountId 
    GROUP BY 
        bk.id,
        ac.fullname,
        ac.avatar,
        sbk.id,
        sbk.status_booking_name,
        iv.id,
        fb.id,
        fb.content,
        fb.stars,
        fr.roomInfo,
        ai.image,
        fr.totalRoom,
        ags.serviceRoomName,
        arsh.serviceHotelName,
        ags.totalsService,
        bk.discount_percent,
        bk.discount_name,
        arsh.totalsServiceHotel
    ORDER BY bk.id DESC
""", nativeQuery = true)
	List<Object[]> findBookingsByAccountIds(@Param("accountId") Integer accountId);
}