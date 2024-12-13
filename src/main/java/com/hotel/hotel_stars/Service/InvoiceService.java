package com.hotel.hotel_stars.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.InvoiceDto;
import com.hotel.hotel_stars.DTO.InvoiceStatisticsDTO;
import com.hotel.hotel_stars.DTO.InvoiceStatisticsDTO2;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.BookingRoom;
import com.hotel.hotel_stars.Entity.Invoice;
import com.hotel.hotel_stars.Entity.Room;
import com.hotel.hotel_stars.Entity.StatusBooking;
import com.hotel.hotel_stars.Entity.StatusRoom;
import com.hotel.hotel_stars.Models.InvoiceModel;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.BookingRoomRepository;
import com.hotel.hotel_stars.Repository.InvoiceRepository;
import com.hotel.hotel_stars.Repository.RoomRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import com.hotel.hotel_stars.Repository.StatusRoomRepository;

@Service
public class InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	BookingRepository bookingRepository;
	@Autowired
	BookingRoomRepository bookingRoomRepository;
	@Autowired
	StatusBookingRepository statusBookingRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	StatusRoomRepository statusRoomRepository;

	public InvoiceDto convertDto(Invoice invoice) {
		InvoiceDto dto = new InvoiceDto();
		dto.setCreateAt(invoice.getCreateAt());
		dto.setId(invoice.getId());
		dto.setInvoiceStatus(invoice.getInvoiceStatus());
		dto.setTotalAmount(invoice.getTotalAmount());
		return dto;
	}

	public List<InvoiceDto> convertListDtos(List<Invoice> invoices) {
		return invoices.stream().map(this::convertDto).toList();
	}

	public InvoiceDto getInvoiceByBooking(Integer id) {
		Invoice invoice = invoiceRepository.findByBooking_Id(id).orElse(null);
		return convertDto(invoice);
	}

	@Transactional
	public StatusResponseDto addInvoice(InvoiceModel invoiceModel) {
		try {
			Booking booking = bookingRepository.findById(invoiceModel.getBookingId())
					.orElseThrow(() -> new IllegalArgumentException("Booking không tồn tại"));
			Invoice invoice = new Invoice();
			invoice.setBooking(booking);
			invoice.setCreateAt(invoiceModel.getCreateAt());
			invoice.setInvoiceStatus(invoiceModel.getInvoiceStatus());
			invoice.setTotalAmount(invoiceModel.getTotalAmount());
			StatusBooking statusBooking = statusBookingRepository.findById(8)
					.orElseThrow(() -> new IllegalArgumentException("Trạng thái đặt phòng không tồn tại"));
			booking.setStatus(statusBooking);
			booking.setStatusPayment(true);
			List<Integer> idBookingRooms = booking.getBookingRooms().stream().map(BookingRoom::getId)
					.collect(Collectors.toList());
			List<BookingRoom> bookingRooms = bookingRoomRepository.findByIdIn(idBookingRooms);
			for (BookingRoom bookingRoom : bookingRooms) {
				Room room = roomRepository.findById(bookingRoom.getRoom().getId())
						.orElseThrow(() -> new IllegalArgumentException("Phòng không tồn tại"));
				StatusRoom statusRoom = statusRoomRepository.findById(5)
						.orElseThrow(() -> new IllegalArgumentException("Trạng thái phòng không tồn tại"));
				room.setStatusRoom(statusRoom);
				bookingRoom.setCheckOut(invoiceModel.getCreateAt());
			}
			bookingRoomRepository.saveAll(bookingRooms);
			roomRepository.saveAll(bookingRooms.stream().map(BookingRoom::getRoom).collect(Collectors.toList()));
			bookingRepository.save(booking);
			invoiceRepository.save(invoice);

			return new StatusResponseDto("200", "success", "Hóa đơn đã được tạo thành công");

		} catch (IllegalArgumentException e) {
			return new StatusResponseDto("404", "error", e.getMessage());
		} catch (Exception e) {
			return new StatusResponseDto("500", "error", "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
		}
	}

	// khoi
	 public InvoiceStatisticsDTO getInvoiceStatistics(LocalDate startDate, LocalDate endDate) {
	        List<Object[]> results = invoiceRepository.getTotalInvoiceStatistics(startDate, endDate);

	        if (!results.isEmpty()) {
	            Object[] result = results.get(0); // Lấy dòng đầu tiên
	            InvoiceStatisticsDTO dto = new InvoiceStatisticsDTO();
	            dto.setTotalRevenue(result[0] != null ? ((Number) result[0]).doubleValue() : 0.0);
	            dto.setRefundedAmount(result[1] != null ? ((Number) result[1]).doubleValue() : 0.0);
	            dto.setNetRevenue(result[2] != null ? ((Number) result[2]).doubleValue() : 0.0);
	            return dto;
	        }
	        return new InvoiceStatisticsDTO(0.0, 0.0, 0.0); // Trả về DTO rỗng nếu không có dữ liệu
	    }
	 
	 public List<InvoiceStatisticsDTO2> getInvoiceStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
	        List<Object[]> rawData = invoiceRepository.getInvoiceStatisticsByDateRange(startDate, endDate);
	        return rawData.stream()
	                .map(row -> {
	                    LocalDate bookingDate = ((java.sql.Date) row[0]).toLocalDate();
	                    Double totalRevenue = (Double) row[1];
	                    Double refundedAmount = (Double) row[2];
	                    Double netRevenue = (Double) row[3];
	                    return new InvoiceStatisticsDTO2(bookingDate, totalRevenue, refundedAmount, netRevenue);
	                })
	                .collect(Collectors.toList());
	    }
	// khoi

}
