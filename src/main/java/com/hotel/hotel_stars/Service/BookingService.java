package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.AmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.Select.*;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.TypeRoomAmenitiesTypeRoomDto;
import com.hotel.hotel_stars.DTO.TypeRoomImageDto;
import com.hotel.hotel_stars.DTO.Select.AccountInfo;
import com.hotel.hotel_stars.DTO.Select.BookingDetailDTO;
import com.hotel.hotel_stars.DTO.Select.CustomerReservation;
import com.hotel.hotel_stars.DTO.Select.PaymentInfoDTO;
import com.hotel.hotel_stars.DTO.Select.ReservationInfoDTO;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.BookingDto;
import com.hotel.hotel_stars.DTO.MethodPaymentDto;
import com.hotel.hotel_stars.DTO.RoleDto;
import com.hotel.hotel_stars.DTO.StatusBookingDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.accountHistoryDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Models.bookingModelNew;
import com.hotel.hotel_stars.Models.bookingRoomModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.utils.paramService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

    public Double calculateDiscountedPrice(Room room, LocalDateTime creatNow, Discount discount, Booking booking) {
        Double typeRoomPrice = room.getTypeRoom().getPrice();
        Instant current = paramServices.localdatetimeToInsant(creatNow);
        if (discount == null) {
            return typeRoomPrice;
        }

        if (room.getTypeRoom().getId() == discount.getTypeRoom().getId()) {
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
        }
        return typeRoomPrice; // Return the final price, ensuring it's positive
    }

    public Boolean checkCreatbkRoom(Integer bookingId, List<Integer> roomId, String discountName) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Discount discount = (discountRepository.findByDiscountName(discountName) != null) ? discountRepository.findByDiscountName(discountName) : null;
        Long days = Duration.between(booking.getStartAt(), booking.getEndAt()).toDays();

        for (int i = 0; i < roomId.size(); i++) {
            Room room = roomRepository.findById(roomId.get(i)).get();
            System.out.println("giá mặc định: " + room.getTypeRoom().getPrice());
            BookingRoom bookingRoom = new BookingRoom();
            Double priceFind = calculateDiscountedPrice(room, booking.getCreateAt(), discount, booking);
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
            bookingRoom.setPrice(priceFind * days);
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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
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

            try {
                bookingRoomRepository.save(bookingRoom);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving BookingRoom", e);
            }
        }
        return true;
    }


    public Booking sendBookingEmail(bookingModel bookingModels) {
        Booking booking = new Booking();
        Optional<Account> accounts = accountRepository.findByUsername(bookingModels.getUserName());
        MethodPayment payment = methodPaymentRepository.findById(bookingModels.getMethodPayment()).get();
        Optional<StatusBooking> statusBooking =
                (payment.getId()==1)?statusBookingRepository.findById(1):statusBookingRepository.findById(3);
        Instant starDateIns = paramServices.stringToInstant(bookingModels.getStartDate());
        Instant endDateIns = paramServices.stringToInstant(bookingModels.getEndDate());
        booking.setAccount(accounts.get());
        booking.setStartAt(starDateIns);
        booking.setEndAt(endDateIns);
        booking.setStatus(statusBooking.get());
        booking.setStatusPayment(false);
        booking.setMethodPayment(payment);
        booking.setCreateAt(LocalDateTime.now());
        try {
            bookingRepository.save(booking);
            if (checkCreatbkRoom(booking.getId(), bookingModels.getRoomId(), bookingModels.getDiscountName())) {
                System.out.println(jwtService.generateBoking(booking.getId()));
                List<BookingRoom> bookingRoomList = booking.getBookingRooms();
                double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
                System.out.println();
                String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
                LocalDate startDate=paramServices.convertInstallToLocalDate(booking.getStartAt());
                LocalDate endDate=paramServices.convertInstallToLocalDate(booking.getEndAt());
                String roomsString = bookingRoomList.stream()
                        .map(bookingRoom -> bookingRoom.getRoom().getRoomName())  // Extract roomName from each BookingRoom
                        .collect(Collectors.joining(", "));

                String idBk = "Bk" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "" + booking.getId();
                Boolean flag = (payment.getId()==1)? paramServices.sendEmails(booking.getAccount().getEmail(), "Xác nhận đặt phòng",
                        paramServices.generateBookingEmail(idBk, booking.getAccount().getFullname(), jwtService.generateBoking(booking.getId())
                                ,startDate,endDate , formattedAmount,roomsString  )):false;

                return booking;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Boolean addBookingOffline(bookingModel bookingModels) {
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
        booking.setMethodPayment(methodPayment);
        booking.setCreateAt(LocalDateTime.now());
        try {
            bookingRepository.save(booking);
            if (checkCreatbkOffRoom(booking.getId(), bookingModels.getRoomId(), bookingModels.getDiscountName())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return false;
    }
    
    public boolean cancelBooking(Integer idBooking) {
        try {
            // Tìm booking theo id
            Booking booking = bookingRepository.findById(idBooking)
                    .orElse(null);
            if (booking == null) {
                return false; // Không tìm thấy booking
            }

            // Cập nhật trạng thái của booking
            StatusBooking statusBooking = statusBookingRepository.findById(6)
                    .orElse(null);
            if (statusBooking == null) {
                return false; // Không tìm thấy trạng thái booking
            }
            booking.setStatus(statusBooking);
            booking.setEndAt(Instant.now());

            // Lấy danh sách phòng từ booking
            List<BookingRoom> bookingRooms = booking.getBookingRooms();
            if (bookingRooms == null || bookingRooms.isEmpty()) {
                return false; // Không có phòng nào trong booking
            }

            // Cập nhật trạng thái phòng
            StatusRoom statusRoom = statusRoomRepository.findById(1)
                    .orElse(null);
            if (statusRoom == null) {
                return false; // Không tìm thấy trạng thái phòng
            }
            for (BookingRoom bookingRoom : bookingRooms) {
                Room room = roomRepository.findById(bookingRoom.getRoom().getId())
                        .orElse(null);
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

    public List<AvailableRoomDTO> getAvailableRoomDTO() {
        List<Object[]> result = bookingRepository.findAvailableRooms();
        List<AvailableRoomDTO> dtos = new ArrayList<>();
        result.forEach(row -> {
            Integer roomId = (Integer) row[0];
            String roomName = (String) row[1];
            String typeRoomName = (String) row[2];
            Integer guestLimit = (Integer) row[3];
            Integer bedCount = (Integer) row[4];
            Double acreage = (Double) row[5];
            String describes = (String) row[6];
            Integer imageId = (Integer) row[7];
            String statusRoomName = (String) row[8];
            Integer typeRoomId = (Integer) row[9];
            Double price = (Double) row[10];
            Integer amenitiesId = (Integer) row[11];

            List<TypeRoomImage> typeRoomImage = typeRoomImageRepository.findByTypeRoomId(typeRoomId);
            List<TypeRoomImageDto> typeRoomImageDtos = new ArrayList<>();
            typeRoomImage.forEach(typeImage -> {
                TypeRoomImageDto typeRoomDto = new TypeRoomImageDto();
                typeRoomDto.setImageName(typeImage.getImageName());
                typeRoomImageDtos.add(typeRoomDto);
            });

            List<TypeRoomAmenitiesTypeRoom> amenitiesTypeRoom = typeRoomAmenitiesTypeRoomRepository
                    .findByTypeRoom_Id(typeRoomId);
            // Create a list to hold the amenities DTOs
            List<TypeRoomAmenitiesTypeRoomDto> amenitiesDtos = new ArrayList<>();

            amenitiesTypeRoom.forEach(amenities -> {
                AmenitiesTypeRoom amenitiesTypeRoomDto = amenitiesTypeRoomRepository
                        .findById(amenities.getAmenitiesTypeRoom().getId()).get();
                AmenitiesTypeRoomDto roomDto = new AmenitiesTypeRoomDto();
                roomDto.setId(amenitiesTypeRoomDto.getId());
                roomDto.setAmenitiesTypeRoomName(amenitiesTypeRoomDto.getAmenitiesTypeRoomName());

                TypeRoomAmenitiesTypeRoomDto typeRoomAmenitiesTypeRoomDto = new TypeRoomAmenitiesTypeRoomDto();
                typeRoomAmenitiesTypeRoomDto.setId(amenities.getId());
                typeRoomAmenitiesTypeRoomDto.setAmenitiesTypeRoomDto(roomDto);

                // Add the created DTO to the list
                amenitiesDtos.add(typeRoomAmenitiesTypeRoomDto);
            });

            AvailableRoomDTO availableRoomDTO = new AvailableRoomDTO();
            availableRoomDTO.setRoomId(roomId);
            availableRoomDTO.setRoomName(roomName);
            availableRoomDTO.setTypeRoomName(typeRoomName);
            availableRoomDTO.setRoomTypeId(typeRoomId);
            availableRoomDTO.setGuestLimit(guestLimit);
            availableRoomDTO.setBedCount(bedCount);
            availableRoomDTO.setArea(acreage);
            availableRoomDTO.setDescription(describes);
            availableRoomDTO.setImages(typeRoomImageDtos);
            availableRoomDTO.setRoomStatus(statusRoomName);
            availableRoomDTO.setPrice(price);
            availableRoomDTO.setAmenities(amenitiesDtos);
            dtos.add(availableRoomDTO);
        });
        return dtos;
    }

    public AccountDto convertToDtoAccount(Account account) {
        // Chuyển đổi Role sang RoleDto
        RoleDto roleDto = new RoleDto(account.getRole().getId(), account.getRole().getRoleName());
        // Chuyển đổi danh sách Booking sang BookingDto
        List<BookingDto> bookingDtoList = account.getBookingList().stream()
                .map(booking -> new BookingDto(booking.getId(), booking.getCreateAt(), booking.getStartAt(),
                        booking.getEndAt(), booking.getStatusPayment(), new AccountDto(),
                        new MethodPaymentDto(booking.getMethodPayment().getId(),
                                booking.getMethodPayment().getMethodPaymentName()))) // Cần xử lý accountDto trong
                // BookingDto
                .collect(Collectors.toList());

        // Trả về AccountDto
        return new AccountDto(
                account.getId(),
                account.getUsername(),
                account.getFullname(),
                account.getPhone(),
                account.getEmail(),
                account.getAvatar(),
                account.getGender(),
                account.getIsDelete(),
                roleDto,
                bookingDtoList);
    }

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
        dto.setStatusBookingDto(new StatusBookingDto(booking.getStatus().getId(), booking.getStatus().getStatusBookingName()));
        dto.setStatusPayment(booking.getStatusPayment());
        dto.setBookingRooms(bookingRoomService.convertListDto(booking.getBookingRooms()));
        dto.setInvoiceDtos(invoiceService.convertListDtos(booking.getInvoice()));

        // Kiểm tra null trước khi tạo MethodPaymentDto
        if (booking.getMethodPayment() != null) {
            dto.setMethodPaymentDto(new MethodPaymentDto(
                    booking.getMethodPayment().getId(),
                    booking.getMethodPayment().getMethodPaymentName()
            ));
        } else {
            dto.setMethodPaymentDto(null); // Hoặc tạo một DTO mặc định
        }

        return dto;
    }


    public List<accountHistoryDto> getAllBooking(String filterType, LocalDate startDate, LocalDate endDate) {
        // Nếu không có filterType, startDate, hoặc endDate, trả về toàn bộ danh sách
        if (filterType == null && startDate == null && endDate == null) {
            return bookingRepository.findAll().stream()
                    .sorted(Comparator.comparing(Booking::getCreateAt).reversed()) // Sắp xếp giảm dần tại đây
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }

        // Gọi repository để lấy danh sách theo điều kiện
        List<Booking> bookings = bookingRepository.findBookingsByTime(filterType, startDate, endDate);

        // Sắp xếp danh sách giảm dần theo createAt
        bookings.sort(Comparator.comparing(Booking::getCreateAt).reversed());

        // Chuyển đổi thành DTO
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public List<accountHistoryDto> getListByAccountId(Integer id) {
        List<Booking> bookings = bookingRepository.findByAccount_Id(id);
        return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private void updateRoomStatus(List<BookingRoom> bookingRooms, Integer statusRoomId) {
        StatusRoom statusRoom = statusRoomRepository.findById(statusRoomId).get();
        
        for (BookingRoom bookingRoom : bookingRooms) {
            Room room = roomRepository.findById(bookingRoom.getRoom().getId()).get();
            room.setStatusRoom(statusRoom);
            roomRepository.save(room);
        }
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
        if (idStatus == 4) {
        	updateRoomStatus(booking.getBookingRooms(), 4);
		}
        booking.setStatus(statusBooking);
        booking.setStartAt(bookingModel.getStartDate());
        booking.setEndAt(bookingModel.getEndDate());
        bookingRepository.save(booking);

        return true;
    }

    public boolean updateStatusCheckInBooking(Integer idBooking, List<Integer> idBookingRoom, List<bookingRoomModel> model) {
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

    public List<accountHistoryDto> getBookingByRoom(Integer idRoom) {
    	List<Booking> bookings = bookingRepository.findBookingsByRoomId(idRoom);
    	return bookings.stream().map(this::convertToDto).toList();
    }
//khôi
}
