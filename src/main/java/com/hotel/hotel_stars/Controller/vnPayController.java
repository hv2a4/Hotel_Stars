package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.VNPayService;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Repository.BookingRepository;
import com.hotel.hotel_stars.Repository.DiscountAccountRepository;
import com.hotel.hotel_stars.Repository.DiscountRepository;
import com.hotel.hotel_stars.Repository.StatusBookingRepository;
import com.hotel.hotel_stars.Service.BookingService;
import com.hotel.hotel_stars.utils.SessionService;
import com.hotel.hotel_stars.utils.paramService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class vnPayController {
    @Autowired
    VNPayService vnPayService;
    @Autowired
    private paramService paramServices;
    @Autowired
    private StatusBookingRepository statusBookingRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountAccountRepository discountAccountRepositorys;
    @Autowired
    SessionService sessionService;
    @Autowired
    BookingService bookingService;
    @GetMapping("/vnpay-payment")
    public void handleVNPayPayment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String orderInfo = request.getParameter("vnp_OrderInfo");
        StatusBooking statusBooking= statusBookingRepository.findById(2).get();
        int paymentStatus = vnPayService.orderReturn(request);
        Booking booking = bookingRepository.findById(Integer.valueOf(orderInfo)).get();
        if( paymentStatus == 1){
            try {
                if (orderInfo != null) {
                    orderInfo = URLDecoder.decode(orderInfo, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            List<BookingRoom> bookingRoomList = booking.getBookingRooms();
            double total = bookingRoomList.stream().mapToDouble(BookingRoom::getPrice).sum();
            if(booking.getDiscountPercent()!=null && booking.getDiscountPercent()!=null){
                double discountAmount = total * (booking.getDiscountPercent() / 100);
                total=total-discountAmount;
            }
System.out.println();
            String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
            LocalDate startDate=paramServices.convertInstallToLocalDate(booking.getStartAt());
            LocalDate endDate=paramServices.convertInstallToLocalDate(booking.getEndAt());
            booking.setStatus(statusBooking);
            String roomsString = bookingRoomList.stream()
                    .map(bookingRoom -> bookingRoom.getRoom().getRoomName())  // Extract roomName from each BookingRoom
                    .collect(Collectors.joining(", "));
            String idBk = "BK" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "TT" + booking.getId();
            booking.setStatusPayment(true);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String startDateStr = startDate.format(formatter);
            String endDateStr = endDate.format(formatter);
            paramServices.sendEmails(booking.getAccount().getEmail(),"thông tin đơn hàng",
                    paramServices.pdfDownload(idBk,booking,startDateStr,endDateStr ,formattedAmount,roomsString, paramServices.getImage()));


            try {
                bookingRepository.save(booking);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String redirectUrl = null;
            try {
                String paymentStatuss = "success";
                String messages = "Bạn đã đặt phòng thành công vui lòng vào email để xem chi tiết đơn đặt hàng và file pdf đã được lưu vào máy của quý khách";
                redirectUrl = String.format(
                        "http://localhost:3000/client/booking-room?status=%s&message=%s",
                        URLEncoder.encode(paymentStatuss, "UTF-8"),
                        URLEncoder.encode(messages, "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Redirecting to URL: " + redirectUrl);

            // Redirect the user to the frontend
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }else{
            if(booking.getDiscountName()!=null && booking.getDiscountPercent()!=null){
                System.out.println("mã này đã hồi phục");
                Discount discount = (discountRepository.findByDiscountName(booking.getDiscountName())!=null)?discountRepository.findByDiscountName(booking.getDiscountName()):null;
                DiscountAccount discountAccount= discountAccountRepositorys.findByDiscountAndAccount(discount.getId(),booking.getAccount().getId());
                discountAccount.setStatusDa(false);
                discountAccountRepositorys.save(discountAccount);
            }
StatusBooking statusBooking1 = statusBookingRepository.findById(6).get();
            booking.setStatus(statusBooking1);
            booking.setStatusPayment(false);
            bookingRepository.save(booking);
            Boolean flag=bookingService.deleteBookingAndRelatedRooms(booking);
            String paymentStatuss = "error";
            String messages = "Thanh toán thất bại";
            String redirectUrl = null;
            redirectUrl = String.format(
                    "http://localhost:3000/client/booking-room?status=%s&message=%s",
                    URLEncoder.encode(paymentStatuss, "UTF-8"),
                    URLEncoder.encode(messages, "UTF-8")
            );

            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }

    }
}