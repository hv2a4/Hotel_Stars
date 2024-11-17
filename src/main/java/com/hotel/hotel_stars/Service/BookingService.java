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
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.DTO.accountHistoryDto;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.bookingModel;
import com.hotel.hotel_stars.Repository.*;
import com.hotel.hotel_stars.Utils.paramService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.Optional;
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
    private StatusBookingRepository statusBookingRepository;

    @Autowired
    TypeRoomImageRepository typeRoomImageRepository;

    @Autowired
    TypeRoomAmenitiesTypeRoomRepository typeRoomAmenitiesTypeRoomRepository;

    @Autowired
    AmenitiesTypeRoomRepository amenitiesTypeRoomRepository;
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

    public Boolean checkCreatbkRoom(Integer bookingId, List<Integer> roomId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        for (int i = 0; i < roomId.size(); i++) {
            Room room = roomRepository.findById(roomId.get(i)).get();
            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setBooking(booking.get());
            bookingRoom.setRoom(room);
            bookingRoom.setPrice(room.getTypeRoom().getPrice());
            try {
                bookingRoomRepository.save(bookingRoom);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public Boolean sendBookingEmail(bookingModel bookingModels) {
        Booking booking = new Booking();
        Optional<Account> accounts = accountRepository.findByUsername(bookingModels.getUserName());
        Optional<MethodPayment> payment = methodPaymentRepository.findById(1);
        Optional<StatusBooking> statusBooking = statusBookingRepository.findById(1);
        Instant starDateIns = paramServices.stringToInstant(bookingModels.getStartDate());
        Instant endDateIns = paramServices.stringToInstant(bookingModels.getEndDate());
        booking.setAccount(accounts.get());
        booking.setStartAt(starDateIns);
        booking.setEndAt(endDateIns);
        booking.setStatus(statusBooking.get());
        booking.setStatusPayment(false);
        System.out.println(LocalDateTime.now());
        booking.setCreateAt(LocalDateTime.now());
        try {
            bookingRepository.save(booking);
            if (checkCreatbkRoom(booking.getId(), bookingModels.getRoomId())) {
                System.out.println(jwtService.generateBoking(booking.getId()));
                Boolean flag = paramServices.sendEmails(booking.getAccount().getEmail(), "Xác nhận đặt phòng",
                        paramServices.generateBooking(booking.getAccount().getFullname(),
                                jwtService.generateBoking(booking.getId())));
                return (flag == true) ? true : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return false;
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

    public CustomerReservation mapToCustomerReservation(Integer bookingId) {
        // Gọi phương thức trong repository
        Optional<CustomerReservation> customerReservation = bookingRepository.findBookingDetailsById(bookingId);

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
        dto.setStatusPayment(booking.getStatusPayment());
        dto.setBookingRooms(bookingRoomService.convertListDto(booking.getBookingRooms()));
        dto.setInvoiceDtos(invoiceService.convertListDtos(booking.getInvoice()));
        dto.setMethodPaymentDto(new MethodPaymentDto(booking.getMethodPayment().getId(),
                booking.getMethodPayment().getMethodPaymentName()));
        return dto;
    }

    public List<accountHistoryDto> getListByAccountId(Integer id) {
        List<Booking> bookings = bookingRepository.findByAccount_Id(id);
        return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
