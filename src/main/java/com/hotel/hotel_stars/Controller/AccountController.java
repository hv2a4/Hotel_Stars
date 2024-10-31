package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.UserInfoService;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Models.changePasswordModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Service.AccountService;
import com.hotel.hotel_stars.Service.TypeRoomService;
import com.hotel.hotel_stars.Utils.paramService;
import jakarta.validation.Valid;
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
import java.util.NoSuchElementException;
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

    @Autowired
    private TypeRoomService typeRoomService;
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/getAlls")
    public ResponseEntity<?> getAllTypeRoom() {
        return ResponseEntity.ok(typeRoomService.getFindTypeRoom());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody accountModel accountModels) {
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
    public ResponseEntity<?> loginAccount(@RequestBody accountModel accounts) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            UserDetails userDetails = userInfoService.loadUserByUsername(accounts.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accounts.getUsername(), accounts.getPasswords()));
            response = paramServices.messageSuccessApi(200, "success", "Đăng Nhập thành công");
            response.put("token", jwtService.generateToken(accounts.getUsername()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(paramServices.messageSuccessApi(400, "fail", "Đăng Nhập thất bại"));
        }
    }

    @PutMapping("/toggleDelete/{id}")
    public ResponseEntity<AccountDto> toggleDeleteStatus(@PathVariable Integer id) {
        AccountDto updatedAccount = accountService.toggleIsDeleteStatus(id);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("get-info-staff")
    public ResponseEntity<?> getInfoStaff() {
        return ResponseEntity.ok(accountService.getAccountBookings());
    }

    @PostMapping("add-account-staff")
    public ResponseEntity<?> addAccountStaff(@Valid @RequestBody accountModel accountModel) {
        try {
            AccountDto createdAccount = accountService.AddAccountStaff(accountModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }

    @PutMapping("update-account-staff/{id}")
    public ResponseEntity<?> updateAccountStaff(@PathVariable Integer id, @Valid @RequestBody accountModel accountModel) {
        try {
            // Gọi phương thức trong service để cập nhật tài khoản
            AccountDto updatedAccount = accountService.UpdateAccountStaff(id, accountModel);
            return ResponseEntity.ok(updatedAccount); // Trả về tài khoản đã cập nhật
        } catch (CustomValidationException ex) {
            // Trả về lỗi xác thực với danh sách thông báo lỗi
            return ResponseEntity.badRequest().body(ex.getErrorMessages());
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
        }
    }

    @DeleteMapping("delete-account-staff/{id}")
    public ResponseEntity<?> deleteAccountStaff(@PathVariable Integer id) {
        try {
            // Gọi phương thức trong service để xóa tài khoản
            accountService.deleteAccountStaff(id);
            return ResponseEntity.ok("Tài khoản đã được xóa thành công."); // Phản hồi thành công
        } catch (NoSuchElementException ex) {
            // Trả về lỗi nếu tài khoản không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại.");
        } catch (RuntimeException ex) {
            // Trả về lỗi chung cho các lỗi không xác thực
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + ex.getMessage());
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

    @PutMapping("changepassword")
    public ResponseEntity<?> changepass(@RequestBody changePasswordModel changePasswordModels){
        Map<String, String> response = new HashMap<String, String>();
        response=  accountService.changeUpdatePass(changePasswordModels);
        if(response.get("code").equals( String.valueOf(400))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("account-by-id/{username}")
    public ResponseEntity<?> getAccountById(@PathVariable("username") String username){
        return ResponseEntity.ok(accountService.getAccountInfoByUsername(username));
    }
}
