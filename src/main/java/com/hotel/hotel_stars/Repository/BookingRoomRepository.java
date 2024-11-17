package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRoomRepository extends JpaRepository<BookingRoom, Integer> {
//	Optional<BookingRoom> findMostRecentBookingRoomByRoomIdAndStatusRoomId(Integer roomId, String statusRoomId);
	@Query("select br from BookingRoom br where br.booking.account.id = ?1")
	List<BookingRoom> findBookingRoomByAccountId(Integer id);
	List<BookingRoom> findByRoom_IdAndRoom_StatusRoom_Id(Integer roomId, Integer statusRoomId);
}