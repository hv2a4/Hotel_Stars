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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    public void sendEmails(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true để chỉ định rằng nội dung là HTML
            emailSender.send(message);
            System.out.println("Email đã được gửi thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Instant stringToInstant(String dateString){
        LocalDate localDate = LocalDate.parse(dateString);
        return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
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
                "<a href=\"http://localhost:3000/login\" class=\"mt-6 inline-block rounded bg-indigo-600 px-5 py-3 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring\">" +
                "Đi đến trang đăng nhập" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    public String generateInvoice(Booking booking, Integer quantityRoom, Double totalAmount,Double price){
        return "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <title>Title</title>\n" +
                "        <meta charset=\"utf-8\" />\n" +
                "        <meta\n" +
                "            name=\"viewport\"\n" +
                "            content=\"width=device-width, initial-scale=1, shrink-to-fit=no\"\n" +
                "        />\n" +
                "        <link\n" +
                "            href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\"\n" +
                "            rel=\"stylesheet\"\n" +
                "            integrity=\"sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN\"\n" +
                "            crossorigin=\"anonymous\"\n" +
                "        />\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"mt-3\" style=\"max-width: 800px; margin: 0 auto; padding: 10px; font-family: Arial, sans-serif; border: 1px solid #ddd;\">\n" +
                "            <div class=\"invoice-content\">\n" +
                "                <header style=\"text-align: center; margin-bottom: 20px;\">\n" +
                "                    <p>Khách sạn Stars</p>\n" +
                "                    <p>Điện thoại: 1900 6522</p>\n" +
                "                </header>\n" +
                "                <section style=\"border-bottom: 1px solid #ddd; padding-bottom: 20px; margin-bottom: 20px;\">\n" +
                "                    <h2 style=\"text-align: center;\">Phiếu đặt phòng</h2>\n" +
                "                    <p style=\"text-align: center;\">Booking_Id: BK0000"+booking.getId()+" </p>\n" +
                "                </section>\n" +
                "                <section style=\"margin-bottom: 20px;\">\n" +
                "                    <p><strong>Tên khách hàng:</strong> "+booking.getAccount().getFullname()+" </p>\n" +
                "                    <p><strong>username:</strong> "+booking.getAccount().getUsername()+" </p>\n" +
                "                    <p><strong>Số điện thoại:</strong>"+booking.getAccount().getPhone()+"</p>\n" +
                "                </section>\n" +
                "                <table style=\"width: 100%; border-collapse: collapse; margin-bottom: 20px;\">\n" +
                "                    <thead>\n" +
                "                        <tr>\n" +
                "                            <th style=\"border: 1px solid #ddd; padding: 8px;\">Nội dung</th>\n" +
                "                            <th style=\"border: 1px solid #ddd; padding: 8px;\">Đơn giá</th>\n" +
                "                            <th style=\"border: 1px solid #ddd; padding: 8px;\">SL</th>\n" +
                "                            <th style=\"border: 1px solid #ddd; padding: 8px;\">Thành tiền</th>\n" +
                "                        </tr>\n" +
                "                    </thead>\n" +
                "                    <tbody>\n" +
                "                        <tr>\n" +
                "                            <td style=\"border: 1px solid #ddd; padding: 8px;\"></td>\n" +
                "                            <td style=\"border: 1px solid #ddd; padding: 8px;\">"+price+"</td>\n" +
                "                            <td style=\"border: 1px solid #ddd; padding: 8px;\">"+quantityRoom+"</td>\n" +
                "                            <td style=\"border: 1px solid #ddd; padding: 8px;\">"+totalAmount+"</td>\n" +
                "                        </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "                <section style=\"margin-bottom: 20px; text-align: right;\">\n" +
                "                    <p><strong>Tổng tiền hàng:</strong> "+totalAmount+" </p>\n" +
                "                    <p><strong>Chiết khấu:</strong> 0</p>\n" +
                "                    <p><strong>Tổng cộng:</strong> "+totalAmount+"</p>\n" +
                "                </section>\n" +
                "                <footer style=\"text-align: center; border-top: 1px solid #ddd; padding-top: 20px;\">\n" +
                "                    <p>Cảm ơn và hẹn gặp lại</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <script\n" +
                "            src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js\"\n" +
                "            integrity=\"sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r\"\n" +
                "            crossorigin=\"anonymous\"\n" +
                "        ></script>\n" +
                "        <script\n" +
                "            src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js\"\n" +
                "            integrity=\"sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+\"\n" +
                "            crossorigin=\"anonymous\"\n" +
                "        ></script>\n" +
                "    </body>\n" +
                "</html>\n";
    }

}
