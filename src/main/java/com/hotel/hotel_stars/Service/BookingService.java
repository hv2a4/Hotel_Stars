package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.Select.*;
import com.hotel.hotel_stars.DTO.Select.AccountInfo;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.CustomerReservation;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.DTO.Select.ReservationInfoDTO;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.selectDTO.BookingHistoryDTOs;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.DeleteBookingModel;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Models.bookingModelNew;
import com.hotel.hotel_stars.Models.bookingRoomModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.utils.paramService;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ErrorsService errorsService;
	@Autowired
	private TypeRoomRepository typeRoomRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private BookingRoomRepository bookingRoomRepository;
	@Autowired
	private MethodPaymentRepository methodPaymentRepository;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	BookingRoomService bookingRoomService;
	@Autowired
	private DiscountRepository discountRepository;
	@Autowired
	private StatusBookingRepository statusBookingRepository;

	@Autowired
	TypeRoomImageRepository typeRoomImageRepository;

	@Autowired
	TypeRoomAmenitiesTypeRoomRepository typeRoomAmenitiesTypeRoomRepository;

	@Autowired
	AmenitiesTypeRoomRepository amenitiesTypeRoomRepository;
	@Autowired
	StatusRoomRepository statusRoomRepository;
	@Autowired
	JwtService jwtService;
	@Autowired
	private paramService paramServices;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private DiscountAccountRepository discountAccountRepositorys;

	public List<BookingDetailDTO> getBookingDetailsByAccountId(Integer accountId) {
		List<Object[]> results = bookingRepository.findBookingDetailsByAccountId(accountId);
		List<BookingDetailDTO> bookingDetails = new ArrayList<>();

		for (Object[] result : results) {
			Integer bookingId = (Integer) result[0];
			String typeRoomName = (String) result[1];
			String roomName = (String) result[2];
			Instant checkIn = (Instant) result[3];
			Instant checkOut = (Instant) result[4];
			Integer numberOfDays = (Integer) result[5];

			BookingDetailDTO dto = new BookingDetailDTO(bookingId, typeRoomName, roomName, checkIn, checkOut,
					numberOfDays);
			bookingDetails.add(dto);
		}
		return bookingDetails;
	}

	public List<PaymentInfoDTO> getPaymentInfoByAccountId(Integer accountId) {
		List<Object[]> results = bookingRepository.findPaymentInfoByAccountId(accountId);
		List<PaymentInfoDTO> paymentInfoDTOs = new ArrayList<>();
		for (Object[] result : results) {
			String methodPaymentName = (String) result[0];
			Boolean status = (Boolean) result[1];
			Double amount = (Double) result[2];

			PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO(methodPaymentName, status, amount);
			paymentInfoDTOs.add(paymentInfoDTO);
		}
		return paymentInfoDTOs;
	}

	public Discount getDiscounts(DiscountAccount discountAccounts, LocalDateTime creatNow) { // getDiscounts nghia
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		if (discountAccounts == null) { // getDiscounts nghia
			return null;
		}
		Instant current = paramServices.localdatetimeToInsant(creatNow);

		LocalDate currentDate = current.atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate discountStartDate = discountAccounts.getDiscount().getStartDate().atZone(ZoneId.systemDefault())
				.toLocalDate();
		LocalDate discountEndDate = discountAccounts.getDiscount().getEndDate().atZone(ZoneId.systemDefault())
				.toLocalDate();

		System.out.println("mã của người này: " + discountAccounts.getId());
		System.out.println("mã của người này: " + discountAccounts.getStatusDa());
		if (!currentDate.isBefore(discountStartDate) && !currentDate.isAfter(discountEndDate)) {
			System.out.println(discountAccounts.getStatusDa());
			if (!discountAccounts.getStatusDa()) {
				System.out.println("Trạng thái ban đầu: " + discountAccounts.getStatusDa());
				discountAccounts.setStatusDa(true);
				discountAccountRepositorys.save(discountAccounts);
				System.out.println("Trạng thái sau khi lưu: " + discountAccounts.getStatusDa());
				return discountAccounts.getDiscount();
			}
		} else {
			System.out.println("ko có giảm giá");
		}
		System.out.println("null rồi nè!");
		return null;
	}

	public Double calculateDiscountedPrice(Room room, LocalDateTime creatNow, Discount discount, Booking booking) {
		Double typeRoomPrice = room.getTypeRoom().getPrice();
		Instant current = paramServices.localdatetimeToInsant(creatNow);
		if (discount == null) {
			return typeRoomPrice;
		}

		LocalDate currentDate = current.atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate discountStartDate = discount.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate discountEndDate = discount.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();
		if (!currentDate.isBefore(discountStartDate) && !currentDate.isAfter(discountEndDate)) {
			double discountRate = discount.getPercent() / 100.0;
			typeRoomPrice = typeRoomPrice * (1 - discountRate);
			booking.setDiscountName(discount.getDiscountName());
			booking.setDiscountPercent(discount.getPercent());
			try {
				bookingRepository.save(booking);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return typeRoomPrice; // Return the final price, ensuring it's positive
	}

	public Boolean checkCreatbkRoom(Integer bookingId, List<Integer> roomId, String discountName) {
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		Booking booking = bookingRepository.findById(bookingId).orElseThrow();
		Discount discount = (discountRepository.findByDiscountName(discountName) != null)
				? discountRepository.findByDiscountName(discountName)
				: null;
		Long days = Duration.between(booking.getStartAt(), booking.getEndAt()).toDays();

		for (int i = 0; i < roomId.size(); i++) {
			Room room = roomRepository.findById(roomId.get(i)).get();
			System.out.println("giá mặc định: " + room.getTypeRoom().getPrice());
			BookingRoom bookingRoom = new BookingRoom();
			Double priceFind = calculateDiscountedPrice(room, booking.getCreateAt(), discount, booking);
			System.out.println("tiền: " + priceFind);
			bookingRoom.setBooking(booking);
			bookingRoom.setRoom(room);
			System.out.println("chưa set: " + room.getTypeRoom().getPrice());
			bookingRoom.setPrice(priceFind * days);
			System.out.println("set rồi: " + bookingRoom.getPrice());
			booking.getBookingRooms().add(bookingRoom);
			try {
				System.out.println("lỗi 1");
				bookingRoomRepository.save(bookingRoom);
				bookingRepository.save(booking);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		System.out.println("mảng: " + booking.getBookingRooms());
		return true;
	}

	public Boolean checkCreatbkOffRoom(Integer bookingId, List<Integer> roomId, String discountName) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		Discount discount = discountRepository.findByDiscountName(discountName);

		// Tính số ngày chính xác
		LocalDate startDate = booking.getStartAt().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = booking.getEndAt().atZone(ZoneId.systemDefault()).toLocalDate();
		Long days = ChronoUnit.DAYS.between(startDate, endDate);
		if (days == 0) {
			days = 1L;
		}

		for (Integer id : roomId) {
			Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
			BookingRoom bookingRoom = new BookingRoom();

			// Tính giá đã áp dụng khuyến mãi
			Double priceFind = calculateDiscountedPrice(room, booking.getCreateAt(), discount, booking);

			// Thiết lập giá tổng (giá mỗi ngày * số ngày)
			bookingRoom.setBooking(booking);
			bookingRoom.setRoom(room);
			bookingRoom.setPrice(priceFind * days);
			bookingRoom.setAccount(booking.getAccount());

			try {
				bookingRoomRepository.save(bookingRoom);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error saving BookingRoom", e);
			}
		}
		return true;
	}

	public Boolean checkCreatbkRoom(Integer bookingId, List<Integer> roomId) { // checkCreatbkRoom nghia
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		Booking booking = bookingRepository.findById(bookingId).orElseThrow(); // checkCreatbkRoom nghia
		LocalDate startDate = booking.getStartAt().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = booking.getEndAt().atZone(ZoneId.systemDefault()).toLocalDate();
		Long days = ChronoUnit.DAYS.between(startDate, endDate);
		if (days == 0) {
			days = 1L;
		}
		System.out.println("ngàt: " + days);
		for (int i = 0; i < roomId.size(); i++) {
			Room room = roomRepository.findById(roomId.get(i)).get();
			System.out.println("giá mặc định: " + room.getTypeRoom().getPrice());
			BookingRoom bookingRoom = new BookingRoom();
			Double priceRoom = room.getTypeRoom().getPrice();
			bookingRoom.setBooking(booking);
			bookingRoom.setRoom(room);
			bookingRoom.setPrice(priceRoom * days);
			System.out.println("giá đã set: " + bookingRoom.getPrice());
			booking.getBookingRooms().add(bookingRoom);
			try {
				System.out.println("lỗi 1");
				bookingRoomRepository.save(bookingRoom);
				bookingRepository.save(booking);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	public Booking sendBookingEmail(bookingModel bookingModels) { // sendBookingEmail nghia
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		Booking booking = new Booking(); // sendBookingEmail nghia
		Optional<Account> accounts = accountRepository.findByUsername(bookingModels.getUserName());
		MethodPayment payment = methodPaymentRepository.findById(bookingModels.getMethodPayment()).get();
		Optional<StatusBooking> statusBooking = (payment.getId() == 1) ? statusBookingRepository.findById(1)
				: statusBookingRepository.findById(3);
		String startDateWithFixedTime = bookingModels.getStartDate().split("T")[0] + "T14:00:00Z";
		String endDateWithFixedTime = bookingModels.getEndDate().split("T")[0] + "T12:00:00Z";
		Instant starDateIns = paramServices.stringToInstant(startDateWithFixedTime).minus(7, ChronoUnit.HOURS);
		Instant endDateIns = paramServices.stringToInstant(endDateWithFixedTime).minus(7, ChronoUnit.HOURS);
		Discount discount = (discountRepository.findByDiscountName(bookingModels.getDiscountName()) != null)
				? discountRepository.findByDiscountName(bookingModels.getDiscountName())
				: null;
		booking.setAccount(accounts.get());
		booking.setStartAt(starDateIns);
		booking.setEndAt(endDateIns);
		booking.setStatus(statusBooking.get());
		booking.setStatusPayment(false);
		booking.setMethodPayment(payment);
		booking.setDescriptions("Đặt trực tuyến");
		System.out.println(LocalDateTime.now());
		booking.setCreateAt(LocalDateTime.now());
		Integer id = (discount != null) ? discount.getId() : null;
		DiscountAccount discountAccount = discountAccountRepositorys.findByDiscountAndAccount(id,
				booking.getAccount().getId());
		Discount discountBooking = getDiscounts(discountAccount, booking.getCreateAt());
		booking.setDiscountName((discountBooking != null) ? discountBooking.getDiscountName() : null);
		booking.setDiscountPercent((discountBooking != null) ? discountBooking.getPercent() : null);
		try {
			bookingRepository.save(booking);
			if (checkCreatbkRoom(booking.getId(), bookingModels.getRoomId())) {
				System.out.println(jwtService.generateBoking(booking.getId()));
				List<BookingRoom> bookingRoomList = booking.getBookingRooms();

				double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
				if (booking.getDiscountName() != null && booking.getDiscountPercent() != null) {
					double discountAmount = (total * booking.getDiscountPercent()) / 100;
					// Cập nhật tổng số tiền sau khi giảm giá
					total = total - discountAmount;
				}
				String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
				LocalDate startDate = paramServices.convertInstallToLocalDate(booking.getStartAt());
				LocalDate endDate = paramServices.convertInstallToLocalDate(booking.getEndAt());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				String startDateStr = startDate.format(formatter);
				String endDateStr = endDate.format(formatter);
				String roomsString = bookingRoomList.stream().map(bookingRoom -> bookingRoom.getRoom().getRoomName()) // Extract
						// roomName
						// from// ea// BookingRoom
						.collect(Collectors.joining(", "));
				String idBk = "BK" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "TT"
						+ booking.getId();
				Boolean flag = (payment.getId() == 1)
						? paramServices.sendEmails(booking.getAccount().getEmail(), "Xác nhận đặt phòng",
								paramServices.generateBookingEmail(idBk, booking.getAccount().getFullname(),
										jwtService.generateBoking(booking.getId()), startDateStr, endDateStr,
										formattedAmount,
										roomsString))
						: false;
				return booking;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}

	public accountHistoryDto addBookingOffline(bookingModel bookingModels) {
		Booking booking = new Booking();
		Optional<Account> accounts = accountRepository.findByUsername(bookingModels.getUserName());
		Optional<StatusBooking> statusBooking = statusBookingRepository.findById(4);
		MethodPayment methodPayment = methodPaymentRepository.findById(1).get();
		String startDateWithFixedTime = bookingModels.getStartDate().split("T")[0] + "T14:00:00Z";
		String endDateWithFixedTime = bookingModels.getEndDate().split("T")[0] + "T12:00:00Z";
		Instant starDateIns = paramServices.stringToInstant(startDateWithFixedTime).minus(7, ChronoUnit.HOURS);
		Instant endDateIns = paramServices.stringToInstant(endDateWithFixedTime).minus(7, ChronoUnit.HOURS);

		booking.setAccount(accounts.get());
		booking.setStartAt(starDateIns);
		booking.setEndAt(endDateIns);
		booking.setStatus(statusBooking.get());
		booking.setStatusPayment(false);
		booking.setDescriptions("Đặt trực tiếp");
		booking.setMethodPayment(methodPayment);
		booking.setCreateAt(LocalDateTime.now());
		try {
			bookingRepository.save(booking);
			if (checkCreatbkOffRoom(booking.getId(), bookingModels.getRoomId(), bookingModels.getDiscountName())) {
				return convertToDto(booking);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}
	public List<ReservationInfoDTO> getAllReservationInfoDTO() {
		List<Object[]> results = bookingRepository.findAllBookingDetailsUsingSQL();
		List<ReservationInfoDTO> dtos = new ArrayList<>();
		for (Object[] row : results) {
			Integer bookingId = (Integer) row[0];
			Integer accountId = (Integer) row[1];
			Integer statusBookingId = (Integer) row[2];
			Integer methodPaymentId = (Integer) row[3];
			Integer bookingRoomId = (Integer) row[4];
			Integer roomId = (Integer) row[5];
			Integer typeRoomId = (Integer) row[6];
			Integer invoiceId = (Integer) row[7];
			String roomName = (String) row[8];
			String methodPaymentName = (String) row[9];
			String statusRoomName = (String) row[10];
			String statusBookingName = (String) row[11];
			Timestamp timestampCreateAt = (Timestamp) row[12];
			LocalDateTime createAt = timestampCreateAt.toLocalDateTime();

			Timestamp timestampStartAt = (Timestamp) row[13];
			LocalDateTime startAt = timestampStartAt.toLocalDateTime();

			Timestamp timestampEndAt = (Timestamp) row[14];
			LocalDateTime endAt = timestampEndAt.toLocalDateTime();
			String accountFullname = (String) row[15];
			String roleName = (String) row[16];
			String typeRoomName = String.valueOf(row[17]);
			Double total_amount = (Double) row[18];
			Integer max_guests = (Integer) row[19];
			// Add to DTO list
			dtos.add(new ReservationInfoDTO(bookingId, accountId, statusBookingId, methodPaymentId,
					bookingRoomId, roomId, typeRoomId, invoiceId, roomName,
					methodPaymentName, statusRoomName, statusBookingName,
					createAt, startAt, endAt, accountFullname, roleName,
					typeRoomName, total_amount, max_guests));
		}
		return dtos;
	}

	public CustomerReservation mapToCustomerReservation(Integer bookingId, String roomName) {
		// Gọi phương thức trong repository
		Optional<CustomerReservation> customerReservation = bookingRepository.findBookingDetailsById(bookingId,
				roomName);

		// Kiểm tra nếu có kết quả
		if (customerReservation.isPresent()) {
			// Nếu có, trả về CustomerReservation
			return customerReservation.get();
		} else {
			// Nếu không có kết quả, có thể ném ngoại lệ hoặc trả về null
			throw new RuntimeException("Booking not found with id " + bookingId);
		}
	}

	public StatusResponseDto updateBookingStatus(Integer bookingId) {
		try {
			// Kiểm tra nếu không tìm thấy booking
			Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
			if (optionalBooking.isEmpty()) {
				return new StatusResponseDto("404", "error", "Không tìm thấy đơn đặt phòng với ID " + bookingId);
			}

			// Tìm status, nếu không có thì ném ngoại lệ
			StatusBooking statusBooking = statusBookingRepository.findById(6)
					.orElseThrow(null);

			// Cập nhật trạng thái
			Booking booking = optionalBooking.get();
			booking.setStatus(statusBooking);
			bookingRepository.save(booking);

			return new StatusResponseDto("200", "success", "Cập nhật trạng thái thành công cho đơn đặt phòng");
		} catch (RuntimeException e) {
			// Bắt lỗi ngoại lệ và trả về phản hồi chi tiết
			return new StatusResponseDto("500", "error", "Đã xảy ra lỗi: " + e.getMessage());
		} catch (Exception e) {
			// Bắt lỗi không xác định khác
			return new StatusResponseDto("500", "error", "Đã xảy ra lỗi không xác định: " + e.getMessage());
		}
	}

//	public List<AvailableRoomDTO> getAvailableRoomDTO() {
//		List<Object[]> result = bookingRepository.findAvailableRooms();
//		List<AvailableRoomDTO> dtos = new ArrayList<>();
//		result.forEach(row -> {
//			Integer roomId = (Integer) row[0];
//			String roomName = (String) row[1];
//			String typeRoomName = (String) row[2];
//			Integer guestLimit = (Integer) row[3];
//			Integer bedCount = (Integer) row[4];
//			Double acreage = (Double) row[5];
//			String describes = (String) row[6];
//			Integer imageId = (Integer) row[7];
//			String statusRoomName = (String) row[8];
//			Integer typeRoomId = (Integer) row[9];
//			Double price = (Double) row[10];
//			Integer amenitiesId = (Integer) row[11];
//
//			List<TypeRoomImage> typeRoomImage = typeRoomImageRepository.findByTypeRoomId(typeRoomId);
//			List<TypeRoomImageDto> typeRoomImageDtos = new ArrayList<>();
//			typeRoomImage.forEach(typeImage -> {
//				TypeRoomImageDto typeRoomDto = new TypeRoomImageDto();
//				typeRoomDto.setImageName(typeImage.getImageName());
//				typeRoomImageDtos.add(typeRoomDto);
//			});
//
//			List<TypeRoomAmenitiesTypeRoom> amenitiesTypeRoom = typeRoomAmenitiesTypeRoomRepository
//					.findByTypeRoom_Id(typeRoomId);
//			// Create a list to hold the amenities DTOs
//			List<TypeRoomAmenitiesTypeRoomDto> amenitiesDtos = new ArrayList<>();
//
//			amenitiesTypeRoom.forEach(amenities -> {
//				AmenitiesTypeRoom amenitiesTypeRoomDto = amenitiesTypeRoomRepository
//						.findById(amenities.getAmenitiesTypeRoom().getId()).get();
//				AmenitiesTypeRoomDto roomDto = new AmenitiesTypeRoomDto();
//				roomDto.setId(amenitiesTypeRoomDto.getId());
//				roomDto.setAmenitiesTypeRoomName(amenitiesTypeRoomDto.getAmenitiesTypeRoomName());
//
//				BookingDto bookingDto = new BookingDto();
//				bookingDto.setId(bookingRoom.getBooking().getId());
//				bookingDto.setCreateAt(bookingRoom.getBooking().getCreateAt());
//				bookingDto.setStartAt(bookingRoom.getBooking().getStartAt());
//				bookingDto.setEndAt(bookingRoom.getBooking().getEndAt());
//				bookingDto.setDescriptions(bookingRoom.getBooking().getDescriptions());
//				bookingDto.setStatusPayment(bookingRoom.getBooking().getStatusPayment());
//				bookingDto.setDiscountPercent(bookingRoom.getBooking().getDiscountPercent());
//				StatusBookingDto statusBookingDto = new StatusBookingDto();
//				statusBookingDto.setStatusBookingName(bookingRoom.getBooking().getStatus().getStatusBookingName());
//				statusBookingDto.setId(bookingRoom.getBooking().getStatus().getId());
//				bookingDto.setStatusDto(statusBookingDto);
//				AccountDto accountBookingDto = new AccountDto();
//				accountBookingDto.setId(bookingRoom.getBooking().getAccount().getId());
//				accountBookingDto.setUsername(bookingRoom.getBooking().getAccount().getUsername());
//				accountBookingDto.setFullname(bookingRoom.getBooking().getAccount().getFullname());
//				accountBookingDto.setPhone(bookingRoom.getBooking().getAccount().getPhone());
//				accountBookingDto.setEmail(bookingRoom.getBooking().getAccount().getEmail());
//				accountBookingDto.setAvatar(bookingRoom.getBooking().getAccount().getAvatar());
//				accountBookingDto.setGender(bookingRoom.getBooking().getAccount().getGender());
//				accountBookingDto.setIsDelete(bookingRoom.getBooking().getAccount().getIsDelete());
//				bookingDto.setAccountDto(accountBookingDto);
//
//				// Add the created DTO to the list
//				amenitiesDtos.add(typeRoomAmenitiesTypeRoomDto);
//			});
//
//			AvailableRoomDTO availableRoomDTO = new AvailableRoomDTO();
//			availableRoomDTO.setRoomId(roomId);
//			availableRoomDTO.setRoomName(roomName);
//			availableRoomDTO.setTypeRoomName(typeRoomName);
//			availableRoomDTO.setRoomTypeId(typeRoomId);
//			availableRoomDTO.setGuestLimit(guestLimit);
//			availableRoomDTO.setBedCount(bedCount);
//			availableRoomDTO.setArea(acreage);
//			availableRoomDTO.setDescription(describes);
//			availableRoomDTO.setImages(typeRoomImageDtos);
//			availableRoomDTO.setRoomStatus(statusRoomName);
//			availableRoomDTO.setPrice(price);
//			availableRoomDTO.setAmenities(amenitiesDtos);
//			dtos.add(availableRoomDTO);
//		});
//		return dtos;
//	}

	public AccountInfo convertDT(Account account) {
		if (account == null) {
			return null; // Hoặc xử lý theo cách bạn muốn
		}
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setId(account.getId());
		accountInfo.setUsername(account.getUsername());
		accountInfo.setFullname(account.getFullname());
		accountInfo.setPasswords(account.getPasswords());
		accountInfo.setGender(account.getGender());
		accountInfo.setEmail(account.getEmail());
		accountInfo.setAvatar(account.getAvatar());
		accountInfo.setPhone(account.getPhone());
		return accountInfo;
	}

	public accountHistoryDto convertToDto(Booking booking) {
		accountHistoryDto dto = new accountHistoryDto();
		dto.setAccountDto(convertDT(booking.getAccount()));
		dto.setCreateAt(booking.getCreateAt());
		dto.setEndAt(booking.getEndAt());
		dto.setId(booking.getId());
		dto.setStartAt(booking.getStartAt());
		dto.setDescriptions(booking.getDescriptions());
		dto.setStatusBookingDto(
				new StatusBookingDto(booking.getStatus().getId(), booking.getStatus().getStatusBookingName()));
		dto.setStatusPayment(booking.getStatusPayment());
		dto.setBookingRooms(bookingRoomService.convertListDto(booking.getBookingRooms()));
		if (booking.getInvoices() != null && !booking.getInvoices().isEmpty()) {
			dto.setInvoiceDtos(invoiceService.convertListDtos(booking.getInvoice()));
		} else {
			dto.setInvoiceDtos(null);
		}

		dto.setDisCountName(booking.getDiscountName());
		dto.setDiscountPercent(booking.getDiscountPercent());

		// Kiểm tra null trước khi tạo MethodPaymentDto
		if (booking.getMethodPayment() != null) {
			dto.setMethodPaymentDto(new MethodPaymentDto(
					booking.getMethodPayment().getId(),
					booking.getMethodPayment().getMethodPaymentName()));
		} else {
			dto.setMethodPaymentDto(null); // Hoặc tạo một DTO mặc định
		}

		return dto;
	}

	public List<accountHistoryDto> getAllBooking(LocalDate startDate, LocalDate endDate) {
		// Nếu không có filterType, startDate, hoặc endDate, trả về toàn bộ danh sách
		if (startDate == null && endDate == null) {
			return bookingRepository.findAll().stream().sorted(Comparator.comparing(Booking::getCreateAt).reversed()) // Sắp
					.map(this::convertToDto).collect(Collectors.toList());
		}

		// Gọi repository để lấy danh sách theo điều kiện
		List<Booking> bookings = bookingRepository.findBookingsByTime(startDate, endDate);

		// Sắp xếp danh sách giảm dần theo createAt
		bookings.sort(Comparator.comparing(Booking::getCreateAt).reversed());

		// Chuyển đổi thành DTO
		return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public List<accountHistoryDto> getListByAccountId(Integer id) {
		List<Booking> bookings = bookingRepository.findByAccount_Id(id);
		return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public boolean updateStatusBooking(Integer idBooking, Integer idStatus, bookingModelNew bookingModel) {
		Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
		if (!bookingOptional.isPresent()) {
			return false;
		}

		Optional<StatusBooking> statusBookingOptional = statusBookingRepository.findById(idStatus);
		if (!statusBookingOptional.isPresent()) {
			return false;
		}

		Booking booking = bookingOptional.get();
		StatusBooking statusBooking = statusBookingOptional.get();

		booking.setStatus(statusBooking);
		booking.setStartAt(bookingModel.getStartDate());
		booking.setEndAt(bookingModel.getEndDate());
		bookingRepository.save(booking);

		return true;
	}

	public boolean updateStatusCheckInBooking(Integer idBooking, List<Integer> idBookingRoom,
			List<bookingRoomModel> model) {
		// Kiểm tra booking tồn tại
		Optional<Booking> bookingOptional = bookingRepository.findById(idBooking);
		if (!bookingOptional.isPresent()) {
			return false;
		}

		// Kiểm tra danh sách idBookingRoom và model
		if (idBookingRoom != null && !idBookingRoom.isEmpty() && model != null && !model.isEmpty()) {
			for (bookingRoomModel br : model) {
				if (idBookingRoom.contains(br.getRoomId())) {
					BookingRoom bookingRoom = bookingRoomRepository.findById(br.getId()).get();
					System.out.println(bookingRoom.getId());
					bookingRoom.setCheckIn(br.getCheckIn());
					bookingRoom.setCheckOut(br.getCheckOut());

					bookingRoomRepository.save(bookingRoom);

					// Cập nhật trạng thái phòng
					Room room = roomRepository.findById(br.getRoomId()).get();
					StatusRoom statusRoom = statusRoomRepository.findById(2).get(); // Đang sử dụng

					room.setStatusRoom(statusRoom);
					System.out.println(room.getStatusRoom().getStatusRoomName());
					roomRepository.save(room);
				}
			}
		}

		// Kiểm tra trạng thái tổng thể của booking
		Booking booking = bookingOptional.get();
		boolean allRoomsCheckedIn = booking.getBookingRooms().stream()
				.allMatch(room -> {
					// Kiểm tra null trước khi lấy trạng thái phòng
					if (room.getRoom() == null || room.getRoom().getStatusRoom() == null) {
						System.out.println("Room or StatusRoom is null");
						return false; // Trả về false nếu có phòng hoặc trạng thái phòng bị null
					}
					return room.getRoom().getStatusRoom().getId() == 2; // Kiểm tra trạng thái "đang sử dụng"
				});

		if (allRoomsCheckedIn) {
			Optional<StatusBooking> optionalStatusBooking = statusBookingRepository.findById(7); // Đang sử dụng
			if (!optionalStatusBooking.isPresent()) {
				return false; // Trả về false nếu không tìm thấy StatusBooking
			}
			StatusBooking statusBooking = optionalStatusBooking.get();
			booking.setStatus(statusBooking);
			bookingRepository.save(booking);
		}

		return true;
	}

	// khoi

	public accountHistoryDto getByIdBooking(Integer id) {
		Booking booking = bookingRepository.findById(id).get();
		return convertToDto(booking);
	}

	// khôi
	public Boolean checkCreatMaintenanceSchedule(Integer bookingId, List<Integer> roomId, String username) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));
		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("account not found"));
		;
		System.out.println(account);
		// Tính số ngày chính xác
		LocalDate startDate = booking.getStartAt().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = booking.getEndAt().atZone(ZoneId.systemDefault()).toLocalDate();
		Long days = ChronoUnit.DAYS.between(startDate, endDate);
		if (days == 0) {
			days = 1L;
		}

		for (Integer id : roomId) {
			Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
			BookingRoom bookingRoom = new BookingRoom();

			// Tính giá đã áp dụng khuyến mãi

			// Thiết lập giá tổng (giá mỗi ngày * số ngày)
			bookingRoom.setBooking(booking);
			bookingRoom.setCheckIn(booking.getStartAt());
			bookingRoom.setRoom(room);
			bookingRoom.setAccount(account);

			try {
				bookingRoomRepository.save(bookingRoom);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error saving BookingRoom", e);
			}
		}
		return true;
	}

	public Boolean createMaintenanceSchedule(bookingModel bookingModels, String usename) {
		System.out.println(bookingModels.getUserName());
		Booking booking = new Booking();
		Optional<Account> accounts = accountRepository.findByUsername(bookingModels.getUserName());
		System.out.println(accounts);
		Optional<StatusBooking> statusBooking = statusBookingRepository.findById(10);
		System.out.println(statusBooking);
		MethodPayment methodPayment = methodPaymentRepository.findById(1).get();
		String startDateWithFixedTime = bookingModels.getStartDate().split("T")[0] + "T14:00:00Z";
		String endDateWithFixedTime = bookingModels.getEndDate().split("T")[0] + "T12:00:00Z";
		Instant starDateIns = paramServices.stringToInstant(startDateWithFixedTime).minus(7, ChronoUnit.HOURS);
		Instant endDateIns = paramServices.stringToInstant(endDateWithFixedTime).minus(7, ChronoUnit.HOURS);

		booking.setAccount(accounts.get());
		booking.setStartAt(starDateIns);
		booking.setEndAt(endDateIns);
		booking.setStatus(statusBooking.get());
		booking.setStatusPayment(false);
		booking.setDescriptions("Bảo trì");
		booking.setMethodPayment(methodPayment);
		booking.setCreateAt(LocalDateTime.now());
		try {
			bookingRepository.save(booking);
			if (checkCreatMaintenanceSchedule(booking.getId(), bookingModels.getRoomId(), usename)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("lỗi đây này");
			throw new RuntimeException(e);
		}
		return false;
	}

	public BookingRoomDto toBookingRoomDTO(BookingRoom bookingRoom) {
		BookingRoomDto bookingRoomDto = modelMapper.map(bookingRoom, BookingRoomDto.class);

		RoleDto roleDto = new RoleDto();
		roleDto.setId(bookingRoom.getBooking().getAccount().getRole().getId());
		roleDto.setRoleName(bookingRoom.getBooking().getAccount().getRole().getRoleName());

		FloorDto floorDto = new FloorDto();
		floorDto.setId(bookingRoom.getRoom().getFloor().getId());
		floorDto.setFloorName(bookingRoom.getRoom().getFloor().getFloorName());

		TypeBedDto typeBedDto = new TypeBedDto();
		typeBedDto.setId(bookingRoom.getRoom().getTypeRoom().getId());
		typeBedDto.setBedName(bookingRoom.getRoom().getTypeRoom().getTypeRoomName());

		AccountDto accountDto = null;
		if (bookingRoom.getAccount() != null) {
			accountDto = new AccountDto();
			accountDto.setId(bookingRoom.getAccount().getId());
			accountDto.setUsername(bookingRoom.getAccount().getUsername());
			accountDto.setFullname(bookingRoom.getAccount().getFullname());
			accountDto.setPhone(bookingRoom.getAccount().getPhone());
			accountDto.setEmail(bookingRoom.getAccount().getEmail());
			accountDto.setAvatar(bookingRoom.getAccount().getAvatar());
			accountDto.setGender(bookingRoom.getAccount().getGender());
			accountDto.setIsDelete(bookingRoom.getAccount().getIsDelete());
			accountDto.setRoleDto(roleDto);
		}

		BookingDto bookingDto = new BookingDto();
		bookingDto.setId(bookingRoom.getBooking().getId());
		bookingDto.setCreateAt(bookingRoom.getBooking().getCreateAt());
		bookingDto.setStartAt(bookingRoom.getBooking().getStartAt());
		bookingDto.setEndAt(bookingRoom.getBooking().getEndAt());
		bookingDto.setDescriptions(bookingRoom.getBooking().getDescriptions());
		bookingDto.setStatusPayment(bookingRoom.getBooking().getStatusPayment());
		bookingDto.setDiscountPercent(bookingRoom.getBooking().getDiscountPercent());
		StatusBookingDto statusBookingDto = new StatusBookingDto();
		statusBookingDto.setStatusBookingName(bookingRoom.getBooking().getStatus().getStatusBookingName());
		statusBookingDto.setId(bookingRoom.getBooking().getStatus().getId());
		bookingDto.setStatusDto(statusBookingDto);
		AccountDto accountBookingDto = new AccountDto();
		accountBookingDto.setId(bookingRoom.getBooking().getAccount().getId());
		accountBookingDto.setUsername(bookingRoom.getBooking().getAccount().getUsername());
		accountBookingDto.setFullname(bookingRoom.getBooking().getAccount().getFullname());
		accountBookingDto.setPhone(bookingRoom.getBooking().getAccount().getPhone());
		accountBookingDto.setEmail(bookingRoom.getBooking().getAccount().getEmail());
		accountBookingDto.setAvatar(bookingRoom.getBooking().getAccount().getAvatar());
		accountBookingDto.setGender(bookingRoom.getBooking().getAccount().getGender());
		accountBookingDto.setIsDelete(bookingRoom.getBooking().getAccount().getIsDelete());
		bookingDto.setAccountDto(accountBookingDto);

		StatusRoomDto statusRoomDto = new StatusRoomDto();
		statusRoomDto.setId(bookingRoom.getRoom().getStatusRoom().getId());
		statusRoomDto.setStatusRoomName(bookingRoom.getRoom().getStatusRoom().getStatusRoomName());

		RoomDto roomDto = new RoomDto();
		roomDto.setId(bookingRoom.getRoom().getId());
		roomDto.setRoomName(bookingRoom.getRoom().getRoomName());
		roomDto.setFloorDto(floorDto);
		roomDto.setStatusRoomDto(statusRoomDto);
		roomDto.setTypeRoomDto(convertTypeRoomDto(bookingRoom.getRoom().getTypeRoom()));
		// Ánh xạ các đối tượng đầy đủ của Booking và Room
		bookingRoomDto.setBooking(bookingDto);
		bookingRoomDto.setAccountDto(accountDto);
		bookingRoomDto.setRoom(roomDto);

		return bookingRoomDto;
	}

	public TypeRoomDto convertTypeRoomDto(TypeRoom tr) {
		TypeBedDto typeBedDto = new TypeBedDto();
		typeBedDto.setId(tr.getTypeBed().getId());
		typeBedDto.setBedName(tr.getTypeBed().getBedName());
		List<TypeRoomImage> typeRoomImages = tr.getTypeRoomImages();

		List<TypeRoomImageDto> typeRoomImageDtos = new ArrayList<>();

		for (TypeRoomImage typeRoomImage : typeRoomImages) {
			TypeRoomImageDto typeRoomImageDto = new TypeRoomImageDto();
			typeRoomImageDto.setId(typeRoomImage.getId()); // Lấy ID của từng ảnh
			typeRoomImageDto.setImageName(typeRoomImage.getImageName()); // Lấy tên ảnh từ từng ảnh

			typeRoomImageDtos.add(typeRoomImageDto); // Thêm vào danh sách DTO
		}
		return new TypeRoomDto(tr.getId(), tr.getTypeRoomName(), tr.getPrice(), tr.getBedCount(), tr.getAcreage(),
				tr.getGuestLimit(), typeBedDto, tr.getDescribes(), typeRoomImageDtos);
	}

	public List<BookingStatisticsDTO> getStatistics(LocalDate startDate, LocalDate endDate) {
		List<Object[]> objects = bookingRepository.getBookingStatisticsByDateRange(startDate, endDate);
		List<BookingStatisticsDTO> result = new ArrayList<>();
		for (Object[] object : objects) {
			java.sql.Date sqlDate = (java.sql.Date) object[0];
			LocalDate bookingDate = sqlDate.toLocalDate();
			Long totalBookings = (Long) object[1];
			Long totalRoomsBooked = (Long) object[2];
			Double totalBookingValue = (Double) object[3];
			Double totalPaid = (Double) object[4];
			BookingStatisticsDTO bookingStatisticsDTO = new BookingStatisticsDTO(bookingDate, totalBookings,
					totalRoomsBooked, totalBookingValue, totalPaid);
			result.add(bookingStatisticsDTO);
		}

		return result;
	}

	public List<accountHistoryDto> getBookingsByStartAtWithInvoice(LocalDate date) {
		List<Booking> bookings = bookingRepository.findBookingsByStartAtWithInvoice(date);
		return bookings.stream().map(this::convertToDto).toList();
	}

	public List<BookingHistoryDTOs> getBookingsByAccountId(Integer accountId) {
		// Lấy danh sách dữ liệu cơ bản
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		List<Object[]> results = bookingRepository.findBookingsByAccountId(accountId);
		return results.stream()
				.map(objects -> {
					// Lấy Booking từ repository bằng bk_id (objects[0])
					Booking booking = bookingRepository.findById((Integer) objects[0])
							.orElseThrow(() -> new RuntimeException("Booking not found with id: " + objects[0]));

					// Truy xuất thông tin bổ sung từ Booking
					Integer methodPaymentId = booking.getMethodPayment().getId();
					String methodPaymentName = booking.getMethodPayment().getMethodPaymentName();
					String discountName = booking.getDiscountName();
					Double discountPercent = booking.getDiscountPercent();
					Integer statusBookingID = booking.getStatus().getId();
					// Tạo BookingHistory	DTOs với thông tin đầy đủ
					return new BookingHistoryDTOs(
							(Integer) objects[0], // bk_id
							(String) objects[1], // bkformat
							(String) objects[2], // create_at
							(String) objects[3], // start_at
							(String) objects[4], // end_at
							(String) objects[5], // fullname
							(String) objects[6], // avatar
							(Integer) objects[7], // statusBkID
							(String) objects[8], // statusBkName
							(Integer) objects[9], // iv_id
							(Double) objects[10], // totalRoom
							(Integer) objects[11], // fb_id
							(String) objects[12], // content
							(Integer) objects[13], // stars
							(String) objects[14], // roomInfo
							(String) objects[15], // image
							(String) objects[16], // combinedServiceNames
							(Double) objects[17], // combinedTotalServices
							(Double) objects[18], // totalAmount
							methodPaymentId, // Lấy từ Booking
							methodPaymentName, // Lấy từ Booking
							discountName, // Lấy từ Booking
							discountPercent, // Lấy từ Booking
							statusBookingID);
				})
				.collect(Collectors.toList());
	}

	public List<accountHistoryDto> getBookingByRoom(Integer idRoom) {
		List<Booking> bookings = bookingRepository.findBookingsByRoomId(idRoom);
		return bookings.stream().map(this::convertToDto).toList();
	}

	public boolean cancelBooking(Integer idBooking, String why) {
		try {
			// Tìm booking theo id
			Booking booking = bookingRepository.findById(idBooking).orElse(null);
			if (booking == null) {
				return false; // Không tìm thấy booking
			}

			// Cập nhật trạng thái của booking
			StatusBooking statusBooking = statusBookingRepository.findById(6).orElse(null);
			if (statusBooking == null) {
				return false; // Không tìm thấy trạng thái booking
			}
			booking.setStatus(statusBooking);
			booking.setDescriptions("Đã hủy vì: " + why);
			booking.setEndAt(Instant.now());

			// Lấy danh sách phòng từ booking
			List<BookingRoom> bookingRooms = booking.getBookingRooms();
			if (bookingRooms == null || bookingRooms.isEmpty()) {
				return false; // Không có phòng nào trong booking
			}

			// Cập nhật trạng thái phòng
			StatusRoom statusRoom = statusRoomRepository.findById(1).orElse(null);
			if (statusRoom == null) {
				return false; // Không tìm thấy trạng thái phòng
			}
			for (BookingRoom bookingRoom : bookingRooms) {
				Room room = roomRepository.findById(bookingRoom.getRoom().getId()).orElse(null);
				if (room == null) {
					return false; // Không tìm thấy phòng
				}
				room.setStatusRoom(statusRoom);
				roomRepository.save(room);
			}

			// Lưu lại booking
			bookingRepository.save(booking);
			return true; // Thành công
		} catch (Exception e) {
			e.printStackTrace(); // In lỗi ra console (có thể thay bằng ghi log)
			return false; // Lỗi phát sinh
		}
	}

	public RoomUsageDTO getRoomUsage(String startDate, String endDate) {

		// Lấy kết quả từ repository
		Object[] result = bookingRepository.findRoomUsage(startDate, endDate);

		// Kiểm tra xem phần tử đầu tiên có phải là mảng không
		if (result != null && result.length > 0) {
			Object firstElement = result[0];
			System.out.println("First element class: " + firstElement.getClass().getName());

			// Nếu phần tử đầu tiên là một mảng con, lấy các giá trị bên trong nó
			if (firstElement instanceof Object[]) {
				Object[] values = (Object[]) firstElement; // Ép kiểu phần tử thành mảng con

				// Kiểm tra và ép kiểu từng giá trị trong mảng con
				if (values.length == 3) {
					Long occupiedRooms = (values[0] instanceof Long) ? (Long) values[0] : 0L;
					Long totalRooms = (values[1] instanceof Long) ? (Long) values[1] : 0L;
					// Sử dụng BigDecimal để xử lý tỷ lệ phần trăm
					BigDecimal usagePercentage = BigDecimal.ZERO;
					if (values[2] instanceof BigDecimal) {
						usagePercentage = (BigDecimal) values[2];
					} else if (values[2] instanceof Double) {
						usagePercentage = BigDecimal.valueOf((Double) values[2]);
					}

					// Làm tròn tỷ lệ phần trăm nếu cần
					usagePercentage = usagePercentage.setScale(2, RoundingMode.HALF_UP);
					RoomUsageDTO roomUsageDTO = new RoomUsageDTO();
					roomUsageDTO.setOccupiedRooms(occupiedRooms);
					roomUsageDTO.setTotalRooms(totalRooms);
					roomUsageDTO.setUsagePercentage(usagePercentage.doubleValue());

					return roomUsageDTO;
				}
			}
		}
		return null; // Trả về null nếu không có kết quả
	}

	public Boolean deleteBookings(DeleteBookingModel bookingModels) {
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		Booking booking = bookingRepository.findById(bookingModels.getBookingId()).get();
		StatusBooking statusBooking = statusBookingRepository.findById(6).orElse(null);
		booking.setStatus(statusBooking);
		booking.setDescriptions(bookingModels.getDescriptions());
		try {
			bookingRepository.save(booking);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Transactional
	public Boolean deleteBookingAndRelatedRooms(Booking booking) {
		// deleteBookingAndRelatedRooms nghia
		// nghia, hàm này được push sáng ngày 18 tháng 12 năm 2024
		try { // deleteBookingAndRelatedRooms nghia
			if (!bookingRepository.existsById(booking.getId())) {
				return false; // Nếu không tồn tại, trả về false
			}
			bookingRoomRepository.deleteByBookingId(booking.getId());
			bookingRepository.delete(booking);
			return true; // Xóa thành công
		} catch (Exception e) {
			return false; // Xóa thất bại
		}
	}
}
