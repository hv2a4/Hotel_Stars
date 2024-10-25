package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.UserInfoService;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Service.AccountService;
import com.hotel.hotel_stars.utils.paramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("api/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    paramService paramServices;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount (@RequestBody accountModel accountModels){
        Map<String, String> response = new HashMap<String, String>();
        System.out.println("password: "+accountModels.getPasswords());
        boolean flag=accountService.addUser(accountModels);
        if (flag) {
            response = paramServices.messageSuccessApi(200, "success", "Đăng ký thành công");
            return ResponseEntity.ok(response); // 200 OK for success
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Đăng ký thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request for failure
        }
    }
    @PostMapping("/loginToken")
    public ResponseEntity<?> loginAccount (@RequestBody accountModel accounts){
        Map<String, String> response = new HashMap<String, String>();
        try {
            UserDetails userDetails = userInfoService.loadUserByUsername(accounts.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accounts.getUsername(), accounts.getPasswords()));
            response=paramServices.messageSuccessApi(200,"success","Đăng Nhập thành công");
            response.put("token", jwtService.generateToken(accounts.getUsername()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(paramServices.messageSuccessApi(400, "fail", "Đăng Nhập thất bại"));
        }
    }
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmailEmployee(@RequestBody Map<String, String> request) {
        Map<Object, Object> response = new HashMap<>();
        String email = request.get("email");
        Boolean result=accountService.sendEmailUpdatePassword(email);
        if(result == false){
            response.put("message", "Email Không tồn tại");
        }
        response.put("message", "Email sent successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam("token") String token) {
        String email = jwtService.extractUsername(token);
        String randomPassword = paramServices.generateTemporaryPassword();
        System.out.println(email);
        Optional<Account> accounts = accountRepository.findByUsername(email);
        String passwords=encoder.encode(randomPassword) ;
        accounts.get().setPasswords(passwords);
        try {
            accountRepository.save( accounts.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
        }
        paramServices.sendEmails(accounts.get().getEmail(), "Mật khẩu mới", "Mật Khẩu mới: "+randomPassword);
        String generateHtmls= paramServices.generateHtml("Thông Báo","Mật khẩu thành công vừa gửi qua email của bạn","Mời Bạn quay về login để đăng nhập");

        return ResponseEntity.ok(generateHtmls);
    }
}
