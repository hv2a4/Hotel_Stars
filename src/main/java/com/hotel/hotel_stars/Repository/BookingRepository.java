package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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



}