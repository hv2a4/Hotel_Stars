package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.DTO.Select.CustomerReservation;
import com.hotel.hotel_stars.DTO.Select.ReservationInfoDTO;
import com.hotel.hotel_stars.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b.id, tr.typeRoomName, r.roomName, br.checkIn, br.checkOut, " +
            "DATEDIFF(br.checkOut, br.checkIn) AS numberOfDays " +
            "FROM Account a " +
            "JOIN a.bookingList b " +
            "JOIN b.bookingRooms br " +
            "JOIN br.room r " +
            "JOIN r.typeRoom tr " +
            "WHERE a.id = :accountId")
    List<Object[]> findBookingDetailsByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT mp.methodPaymentName, b.statusPayment, invoice.totalAmount " +
            "FROM Booking b " +
            "JOIN b.methodPayment mp " +
            "JOIN b.account a " +
            "JOIN Invoice invoice ON b.id = invoice.booking.id " +
            "WHERE a.id = :accountId")
    List<Object[]> findPaymentInfoByAccountId(@Param("accountId") Integer accountId); // Thêm @Param

    @Query(value = "SELECT booking.id, " +
            "accounts.id, " +
            "status_booking.id, " +
            "method_payment.id, " +
            "booking_room.id, " +
            "room.id, " +
            "type_room.id, " +
            "invoice.id, " +
            "room.room_name, " +
            "method_payment.method_payment_name, " +
            "status_room.status_room_name, " +
            "status_booking.status_booking_name, " +
            "booking.create_at, " +
            "booking.start_at, " +
            "booking.end_at, " +
            "accounts.fullname, " +
            "roles.role_name, " +  // Sửa lỗi ở đây: thêm dấu phẩy
            "type_room.type_room_name, " +  // Không cần dấu phẩy ở cuối dòng này
            "invoice.total_amount, " +
            "type_room.guest_limit AS max_guests " +
            "FROM accounts " +
            "JOIN booking ON accounts.id = booking.account_id " +
            "JOIN status_booking ON booking.status_id = status_booking.id " +
            "JOIN method_payment ON booking.method_payment_id = method_payment.id " +
            "JOIN booking_room ON booking.id = booking_room.booking_id " +
            "JOIN room ON booking_room.room_id = room.id " +
            "JOIN type_room ON room.type_room_id = type_room.id " +
            "JOIN invoice ON booking.id = invoice.booking_id " +
            "JOIN status_room ON room.status_room_id = status_room.id " +
            "JOIN roles ON accounts.role_id = roles.id", nativeQuery = true)
    List<Object[]> findAllBookingDetailsUsingSQL();

    @Query("SELECT new com.hotel.hotel_stars.DTO.Select.CustomerReservation(" +
            "a.id, a.fullname, a.phone, a.email, " +
            "b.id, b.startAt, b.endAt, " +
            "tr.guestLimit, tr.typeRoomName, r.roomName, " +
            "tr.price, i.totalAmount, sb.statusBookingName, " +
            "mp.methodPaymentName, b.statusPayment, role.roleName) " +
            "FROM Account a " +
            "JOIN Booking b ON a.id = b.account.id " +
            "JOIN Invoice i ON b.id = i.booking.id " +
            "JOIN MethodPayment mp ON b.methodPayment.id = mp.id " +
            "JOIN StatusBooking sb ON b.status.id = sb.id " +
            "JOIN BookingRoom br ON b.id = br.booking.id " +
            "JOIN Room r ON br.room.id = r.id " +
            "JOIN TypeRoom tr ON r.typeRoom.id = tr.id " +
            "JOIN Role role ON a.role.id = role.id " +
            "WHERE b.id = :bookingId")
    Optional<CustomerReservation> findBookingDetailsById(@Param("bookingId") Integer bookingId);


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

}