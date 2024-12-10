package com.hotel.hotel_stars.Repository;

import com.hotel.hotel_stars.Entity.BookingRoomServiceRoom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRoomServiceRoomRepository extends JpaRepository<BookingRoomServiceRoom, Integer> {
	List<BookingRoomServiceRoom> findByBookingRoomIdIn(List<Integer> bookingRoomIds);
	List<BookingRoomServiceRoom> findByBookingRoomId(Integer bookingRoomId);
	List<BookingRoomServiceRoom> findByBookingRoomIdInAndServiceRoomId(List<Integer> bookingRoomIds, Integer serviceRoomId);
}