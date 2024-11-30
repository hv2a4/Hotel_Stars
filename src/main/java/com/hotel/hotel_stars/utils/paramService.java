package com.hotel.hotel_stars.utils;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Entity.Role;
import com.hotel.hotel_stars.Repository.RoleRepository;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;


@Service
public class paramService {
    @Autowired
    RoleRepository rolesRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JavaMailSender emailSender;

    public Account getTokenGG(String token) {
        Account accounts = new Account();
        Optional<Role> roles = rolesRepository.findById(3);
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
            System.out.println("userId: " + userId);
            System.out.println("email: " + email);
            System.out.println("emailVerified: " + emailVerified);
            System.out.println("name: " + name);
            System.out.println("pictureUrl: " + pictureUrl);
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

    public LocalDate convertStringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

    public Map<String, String> messageSuccessApi(Integer code, String status, String message) {
        Map<String, String> response = new HashMap<String, String>();
        response.put("code", "" + code);
        response.put("status", status);
        response.put("message", message);
        return response;
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
            return null;
        }
    }

    public Instant stringToInstant(String dateString) {
        try {
            // Định dạng đầy đủ với thời gian và múi giờ
            DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                                                .withZone(ZoneId.of("UTC"));
            return Instant.from(fullFormatter.parse(dateString));
        } catch (Exception e) {
            DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                                    .withZone(ZoneId.of("UTC"));
            LocalDate localDate = LocalDate.parse(dateString, dateOnlyFormatter);
            return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
        }
    }


    public Instant localdatetimeToInsant(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of("UTC");
        return localDateTime.atZone(zoneId).toInstant();
    }
    public Instant localDateToInstant(LocalDateTime localDateTime ) {
        ZonedDateTime vietnamTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Saigon"));
        System.out.println("thời gian2: " + LocalDateTime.now());
        Instant instantNow = vietnamTime.toInstant();
        System.out.println("thời gian1: " + instantNow);
        return instantNow;
    }
    public LocalDate convertInstallToLocalDate(Instant install) {
        // Convert Instant to LocalDate using the system default timezone
        return install.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String generateHtml(String title, String message, String content) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<title>" + title + "</title>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
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




    public String confirmBookings(String id, Booking booking, LocalDate startDate, LocalDate endDate, String total, String rooms) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Xác Nhận Đặt Phòng PDF</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        body {\n" +
                "            background-color: #eeeeee;\n" +
                "            font-family: 'Roboto', sans-serif;\n" +
                "            height: 100vh;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #F44336;\n" +
                "            padding: 28px;\n" +
                "            text-align: center;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 36px;\n" +
                "            font-weight: 800;\n" +
                "        }\n" +
                "        h2 {\n" +
                "            font-size: 30px;\n" +
                "            font-weight: 800;\n" +
                "            color: #333333;\n" +
                "            margin: 20px 0;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin: 15px 0;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            padding: 10px;\n" +
                "            text-align: left;\n" +
                "            border-bottom: 1px solid #eeeeee;\n" +
                "        }\n" +
                "        th {\n" +
                "            background-color: #eeeeee;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .total {\n" +
                "            font-size: 17px;\n" +
                "            font-weight: 800;\n" +
                "            border-top: 3px solid #eeeeee;\n" +
                "            border-bottom: 3px solid #eeeeee;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            font-weight: 200;\n" +
                "            margin: 20px 0 0 0;\n" +
                "            color: #666666;\n" +
                "        }\n" +
                "        .download-link {\n" +
                "            display: block;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "            text-decoration: none;\n" +
                "            color: #F44336;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Hotel Start</h1>\n" +
                "        </div>\n" +
                "        <h2>Đơn Đặt Phòng</h2>\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <th>Mã đặt phòng</th>\n" +
                "                <td>" + id + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Tên khách hàng</th>\n" +
                "                <td>" + booking.getAccount().getFullname() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Tên tài khoản</th>\n" +
                "                <td>" + booking.getAccount().getUsername() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Số điện thoại</th>\n" +
                "                <td>" + booking.getAccount().getPhone() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Ngày nhận</th>\n" +
                "                <td>" + startDate + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Ngày trả</th>\n" +
                "                <td>" + endDate + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Phòng</th>\n" +
                "                <td>" + rooms + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Trạng thái thanh toán</th>\n" +
                "                <td>" + (booking.getStatusPayment() ? "Đã thanh toán" : "Chưa thanh toán") + "</td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "        <div class=\"total\">\n" +
                "            <span>Tổng tiền</span>\n" +
                "            <span>" + total + "</span>\n" +
                "        </div>\n" +
                "        <a href=\"http://localhost:8080/api/booking/downloadPdf?id=" + booking.getId() + "\" class=\"download-link\">Tải xuống PDF</a>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
    public String generateBookingEmail(String id, String fullName, String token, LocalDate startDate, LocalDate endDate, String total, String rooms) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"vi\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Xác Nhận Đặt Phòng</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            color: black; /* Đặt màu chữ chính là màu đen */\n"
                + "            font-family: Arial, sans-serif;\n"
                + "        }\n"
                + "        h2, p {\n"
                + "            color: black; /* Đảm bảo tiêu đề và đoạn văn có màu đen */\n"
                + "        }\n"
                + "        .button {\n"
                + "            background-color: #4CAF50;\n"
                + "            color: white !important; /* Đặt màu chữ cho nút là màu trắng */\n"
                + "            padding: 10px 15px;\n"
                + "            text-decoration: none;\n"
                + "            border-radius: 5px;\n"
                + "            display: inline-block;\n"
                + "            border: none;\n"
                + "        }\n"
                + "        table {\n"
                + "            width: 100%;\n"
                + "            border-collapse: collapse;\n"
                + "        }\n"
                + "        table, th, td {\n"
                + "            border: 1px solid black;\n"
                + "        }\n"
                + "        th, td {\n"
                + "            padding: 8px;\n"
                + "            text-align: left;\n"
                + "            color: black; /* Đảm bảo màu chữ trong bảng là màu đen */\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <h2>Xác Nhận Đặt Phòng</h2>\n"
                + "    <p>Xin chào <strong>" + fullName + "</strong>,</p>\n"
                + "    <p>Vui lòng xem thông tin đơn đặt phòng của bạn bên dưới:</p>\n"
                + "    <table>\n"
                + "        <tr><th>Ngày nhận phòng</th><td>" + startDate + "</td></tr>\n"
                + "        <tr><th>Ngày trả phòng</th><td>" + endDate + "</td></tr>\n"
                + "        <tr><th>Danh sách phòng</th><td>" + rooms + "</td></tr>\n"
                + "        <tr><th>Tổng tiền</th><td>" + total + "</td></tr>\n"
                + "    </table>\n"
                + "    <p>Để xác nhận đơn đặt phòng, vui lòng nhấn nút bên dưới:</p>\n"
                + "    <a href=\"http://localhost:8080/api/booking/confirmBooking?token=" + token + "\" class=\"button\">Xác Nhận Đặt Phòng</a>\n"
                + "    <p>Trân trọng,<br>Hotel Start</p>\n"
                + "</body>\n"
                + "</html>";
    }
    public String pdfDowload(String id, Booking booking, LocalDate startDate, LocalDate endDate, String total, String rooms) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Xác Nhận Đặt Phòng PDF</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        body {\n" +
                "            background-color: #fff;\n" +
                "            font-family: 'Roboto', sans-serif;\n" +
                "            height: 100vh;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #F44336;\n" +
                "            padding: 28px;\n" +
                "            text-align: center;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 36px;\n" +
                "            font-weight: 800;\n" +
                "        }\n" +
                "        h2 {\n" +
                "            font-size: 30px;\n" +
                "            font-weight: 800;\n" +
                "            color: #333333;\n" +
                "            margin: 20px 0;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin: 15px 0;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            padding: 10px;\n" +
                "            text-align: left;\n" +
                "            border-bottom: 1px solid #eeeeee;\n" +
                "        }\n" +
                "        th {\n" +
                "            background-color: #eeeeee;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .total {\n" +
                "            font-size: 17px;\n" +
                "            font-weight: 800;\n" +
                "            border-top: 3px solid #eeeeee;\n" +
                "            border-bottom: 3px solid #eeeeee;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            font-weight: 200;\n" +
                "            margin: 40px 0 0 0;\n" +
                "            color: #666666;\n" +
                "        }\n" +
                "        .download-link {\n" +
                "            display: block;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "            text-decoration: none;\n" +
                "            color: #F44336;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Hotel Start</h1>\n" +
                "        </div>\n" +
                "        <h2>Đơn Đặt Phòng</h2>\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <th>Mã đặt phòng</th>\n" +
                "                <td>" + id + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Tên khách hàng</th>\n" +
                "                <td>" + booking.getAccount().getFullname() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Tên tài khoản</th>\n" +
                "                <td>" + booking.getAccount().getUsername() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Số điện thoại</th>\n" +
                "                <td>" + booking.getAccount().getPhone() + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Ngày nhận</th>\n" +
                "                <td>" + startDate + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Ngày trả</th>\n" +
                "                <td>" + endDate + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Phòng</th>\n" +
                "                <td>" + rooms + "</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <th>Trạng thái thanh toán</th>\n" +
                "                <td>" + (booking.getStatusPayment() ? "Đã thanh toán" : "Chưa thanh toán") + "</td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "        <div class=\"total\">\n" +
                "            <span>Tổng tiền</span>\n" +
                "            <span>" + total + "</span>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
    public String generatePdf(String htmlContent, String fullName, String id) throws Exception {
        // Định nghĩa đường dẫn thư mục người dùng (Downloads)
        String userHome = System.getProperty("user.home");
        String downloadDir = userHome + "/Downloads/";

        // Tạo tên file theo định dạng HotelStart_fullName_id.pdf
        String fileName = String.format("HotelStart_%s_%s.pdf", fullName.replaceAll("\\s+", "_"), id);
        String filePath = downloadDir + fileName;
        File file = new File(filePath);

        // Kiểm tra nếu file đã tồn tại, tạo tên mới theo định dạng với số thứ tự
        int count = 1;
        while (file.exists()) {
            fileName = String.format("HotelStart_%s_%s(%d).pdf", fullName.replaceAll("\\s+", "_"), id, count);
            filePath = downloadDir + fileName;
            file = new File(filePath);
            count++;
        }

        // Khởi tạo tài liệu PDF
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

        // Mở tài liệu
        document.open();

        try {
            // Cấu hình font tùy chỉnh (ví dụ: Times New Roman hoặc Roboto)
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            fontProvider.register("src/main/resources/fonts/Roboto-Regular.ttf", "Roboto");

            // Sử dụng XMLWorkerHelper để chuyển đổi HTML sang PDF
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            InputStream htmlStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
            worker.parseXHtml(writer, document, htmlStream, null, Charset.forName("UTF-8"), fontProvider);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi xử lý nội dung HTML.");
        } finally {
            // Đóng tài liệu
            document.close();
        }

        // Trả về đường dẫn file PDF
        return filePath;
    }
    public String contentEmail(String token){
        return  "<!DOCTYPE html>\n"
                + "<html lang=\"vi\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Xác Nhận Đổi Mật Khẩu</title>\n"
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
                + "    <h2 style=\"color: #000;\">Xác Nhận Đổi Mật Khẩu</h2>\n" // Màu chữ cho tiêu đề
                + "    <p>Xin chào Bạn</p>\n"
                + "    <p>Bạn đã yêu cầu thay đổi mật khẩu cho tài khoản của mình tại Hotel Start.</p>\n"
                + "    <p>Để xác nhận việc thay đổi mật khẩu, vui lòng nhấp vào liên kết bên dưới:</p>\n"
                + "    <p><a href=\"http://localhost:8080/api/account/updatePassword?token="+token+"\" class=\"button\" style=\"color: white;\">Xác Nhận Đổi Mật Khẩu</a></p>\n"
                + "    <p>Nếu bạn không yêu cầu thay đổi mật khẩu, xin vui lòng bỏ qua email này hoặc liên hệ với chúng tôi để được hỗ trợ.</p>\n"
                + "    <p>Trân trọng,<br>Hotel Start</p>\n"
                + "</div>\n"
                + "</body>\n"
                + "</html>";
    }
}
