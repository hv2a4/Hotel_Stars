package com.hotel.hotel_stars.Scheduler;


import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingExpirationJob {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    StatusBookingRepository statusBookingRepository;
    @Scheduled(fixedRate = 300000) // 2 phút = 120000 or 15 phút = 900000 or 5 phút = 300000 (120000 ms) â
    public void cancelExpiredBookings() {
        List<Booking> bookings=bookingRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        StatusBooking statusBooking=statusBookingRepository.findById(6).get();
        bookings.stream()
                .filter(booking -> booking.getCreateAt().plusMinutes(15).isBefore(now)
                        && (booking.getStatus().getId() == 1 || booking.getStatus().getId() == 2)) // Lọc các booking đã hết hạn
                .forEach(booking -> {
                    System.out.println("id: "+booking.getId());
                    booking.setDescriptions("Hủy do chưa xác nhận!");
                    booking.setStatus(statusBooking); // Cập nhật trạng thái
                    bookingRepository.save(booking); // Lưu thay đổi
                });
    }
}
