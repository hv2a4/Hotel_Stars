package com.hotel.hotel_stars.utils;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hotel.hotel_stars.Entity.Account;
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
        Optional<Role> roles=rolesRepository.findById(4);
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
}
