package com.hotel.hotel_stars.utils;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Role;
import com.hotel.hotel_stars.Repository.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class paramService {
     @Autowired
     RoleRepository rolesRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JavaMailSender emailSender;
    public Account getTokenGG(String token){
        Account accounts=new Account();
        Optional<Role> roles=rolesRepository.findById(3);
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("435509292296-0rf1v3tbl70s3ae1dd1ose1hmv146iqn.apps.googleusercontent.com")) // Replace with your client ID
                    .build();
            GoogleIdToken idToken = verifier.verify(token);
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            System.out.println("userId: "+userId);
            System.out.println("email: "+email);
            System.out.println("emailVerified: "+emailVerified);
            System.out.println("name: "+name);
            System.out.println("pictureUrl: "+pictureUrl);
            accounts.setUsername(email);
            accounts.setEmail(email);
            accounts.setAvatar(pictureUrl);
            accounts.setFullname(name);
            accounts.setRole(roles.get());
            accounts.setGender(true);
            accounts.setIsDelete(true);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }
    public Map<String,String> messageSuccessApi(Integer code,String status,String message){
        Map<String, String> response = new HashMap<String, String>();
        response.put("code", ""+code);
        response.put("status",status);
        response.put("message",message);
        return  response;
    }
    public String generateTemporaryPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }

        return password.toString();
    }
    public Boolean sendEmails(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true để chỉ định rằng nội dung là HTML
            emailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Instant stringToInstant(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }
    public Instant localdatetimeToInsant(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of("UTC");
        return localDateTime.atZone(zoneId).toInstant();
    }
    public Instant localDateToInstant(LocalDateTime localDateTime ) {
        ZonedDateTime vietnamTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Saigon"));
        System.out.println("thời gian2: "+ LocalDateTime.now());
        Instant instantNow = vietnamTime.toInstant();
        System.out.println("thời gian1: "+instantNow);
        return instantNow;
    }

    public String generateHtml(String title, String message,String content) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + title + "</title>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">" +
                "</head>" +
                "<body>" +
                "<div class=\"grid h-screen place-content-center bg-white px-4\">" +
                "<div class=\"text-center\">" +
                "<h1 class=\"text-9xl font-black text-gray-200\">" + title + "</h1>" +
                "<p class=\"text-2xl font-bold tracking-tight text-gray-900 sm:text-4xl\">" + message + "</p>" +
                "<p class=\"mt-4 text-gray-500\">" + content + "</p>" +
                "<a href=\"http://localhost:8080?token=\" class=\"mt-6 inline-block rounded bg-indigo-600 px-5 py-3 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring\">" +
                "Đi đến trang đăng nhập" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    public String generateBooking(String fullName,String token){
        return "<!DOCTYPE html>\n"
                + "<html lang=\"vi\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Xác Nhận Nhận Phòng</title>\n"
                + "    <style>\n"
                + "        .button {\n"
                + "            background-color: #4CAF50;\n"
                + "            color: white;\n"
                + "            padding: 10px 15px;\n"
                + "            text-decoration: none;\n"
                + "            border-radius: 5px;\n"
                + "            display: inline-block;\n" // Để nút có thể được căn giữa
                + "            margin-top: 10px;\n" // Khoảng cách trên nút
                + "        }\n"
                + "        p {\n"
                + "            color: #000; /* Màu chữ cho các đoạn văn */\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "<div class=\"container\">\n"
                + "    <h2 style=\"color: #000;\">Xác Nhận Nhận Phòng</h2>\n" // Màu chữ cho tiêu đề
                + "    <p>Xin chào <strong>"+fullName+"</strong>,</p>\n"
                + "    <p>Cảm ơn bạn đã đặt phòng tại Hotel Start. Chúng tôi rất vui được chào đón bạn!</p>\n"
                + "    <p>Để xác nhận rằng bạn đã đặt phòng, hãy nhấp vào liên kết bên dưới:</p>\n"
                + "    <p><a href=\"http://localhost:8080/api/booking/confirmBooking?token="+token+"\" class=\"button\" style=\"color: white;\">Xác Nhận Nhận Phòng</a></p>\n"
                + "    <p>Nếu bạn có bất kỳ câu hỏi nào, đừng ngần ngại liên hệ với chúng tôi.</p>\n"
                + "    <p>Trân trọng,<br>Hotel Start</p>\n"
                + "</div>\n"
                + "</body>\n"
                + "</html>";
    }


    public String confirmBookings(Booking booking,String total){

          return   "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title></title>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
            "</head>\n" +
            "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n" +
            "\n" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "    <tr>\n" +
            "        <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n" +
            "        \n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\n" +
            "            <tr>\n" +
            "                <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 28px;\" bgcolor=\"#F44336\">\n" +
            "                \n" +
            "                <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\n" +
            "                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n" +
            "                        <tr>\n" +
            "                            <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\n" +
            "                                <h1 style=\"font-size: 36px; font-weight: 800; margin: 0; color: #ffffff;\">Hotel Start</h1>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "                </div>\n" +
            "                \n" +
            "                <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\" class=\"mobile-hide\">\n" +
            "                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n" +
            "                        <tr>\n" +
            "                            <td align=\"right\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; line-height: 48px;\">\n" +
            "                                <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"right\">\n" +
            "                                    <tr>\n" +
            "                                    </tr>\n" +
            "                                </table>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                    </table>\n" +
            "                </div>\n" +
            "                </td>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td align=\"center\" style=\"padding: 9px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\n" +
            "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\n" +
            "                    <tr>\n" +
            "                        <td align=\"center\" style=\"font-family: Verdana, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\n" +
            "                            <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\">\n" +
            "                                Đơn Đặt Phòng\n" +
            "                            </h2>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                    \n" +
            "                    <tr>\n" +
            "                        <td align=\"left\" style=\"padding-top: 30px;\">\n" +
            "                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Verdana, sans-serif;; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">\n" +
            "                                       Mã đặt phòng\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">\n" +
            "                                       "+booking.getId()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\">\n" +
            "                                        Tên khách hàng\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\">\n" +
            "                                        "+booking.getAccount().getFullname()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        Tên tài khoản\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        "+booking.getAccount().getUsername()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                       Số điện thoại\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        "+booking.getAccount().getPhone()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        Ngày nhận\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        "+booking.getStartAt()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        Ngày trả\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        "+booking.getEndAt()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        Số Lượng Phòng\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 5px 10px;\">\n" +
            "                                        "+booking.getBookingRooms().size()+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n"+
            "                            </table>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td align=\"left\" style=\"padding-top: 20px;\">\n" +
            "                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
            "                                <tr>\n" +
            "                                    <td width=\"75%\" align=\"left\" style=\"font-family: Verdana, sans-serif; font-size: 17px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\">\n" +
            "                                        Tổng tiền\n" +
            "                                    </td>\n" +
            "                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\">\n" +
            "                                       "+total+"\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                            </table>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "                </td>\n" +
            "            </tr>\n" +
            "             <tr>\n" +
            "                <td align=\"center\" height=\"100%\" valign=\"top\" width=\"100%\" style=\"padding: 0 35px 10px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\n" +
            "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:660px;\">\n" +
            "                    <tr>\n" +
            "                        <td align=\"center\" valign=\"top\" style=\"font-size:0;\">\n" +
            "                           \n" +
            "                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\n" +
            "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n" +
            "                                    <tr>\n" +
            "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 15px; font-weight: 400; line-height: 24px;\">\n" +
            "                                            <p style=\"font-weight: 200; text-align: center;\">Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi</p>\n" +
            "                                        </td>\n" +
            "                                    </tr>\n" +
            "                                </table>\n" +
            "                            </div>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "                </td>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "</table>\n" +
            "    \n" +
            "</body>\n" +
            "</html>";
    }
}
