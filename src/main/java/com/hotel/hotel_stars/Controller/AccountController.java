package com.hotel.hotel_stars.Controller;

import java.util.*;

import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Booking;
import com.hotel.hotel_stars.Exception.ErrorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.UserInfoService;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Models.changePasswordModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Service.AccountService;
import com.hotel.hotel_stars.Service.TypeRoomService;
import com.hotel.hotel_stars.utils.paramService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/account")
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
    @Autowired
    private ErrorsService errorsServices;

    // @GetMapping("/getAll")
    // public ResponseEntity<?> getAllAccounts( @RequestParam (defaultValue = "0",
    // required = false) Integer id) {
    //
    // return ResponseEntity.ok("ạksfasj");
    // }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(accountService.getAccountId(id));
    }

    @PostMapping("/getTokenGG")
    public ResponseEntity<?> getToken(@RequestBody accountModel accountModels) {
        Map<String, String> response = new HashMap<String, String>();
        System.out.println("mã token gg" + accountModels.getEmail());
        String token = accountService.loginGG(accountModels.getEmail());
        if (token == null) {
            response = paramServices.messageSuccessApi(400, "error", "Email này đã tồn tại");
            response.put("token", null);
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        System.out.println(token + "  token22");
        String result = (token != null) ? token : null;
        response.put("token", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody accountModel accountModels) {
        StatusResponseDto statusResponseDto = errorsServices.errorRegister(accountModels);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        Map<String, String> response = new HashMap<String, String>();
        boolean flag = accountService.addUser(accountModels);
        if (flag) {
            response = paramServices.messageSuccessApi(201, "success", "Đăng ký thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Đăng ký thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request for failure
        }
    }

    @PostMapping("/loginToken")
    public ResponseEntity<?> loginAccount(@RequestBody accountModel accounts) {
        Map<String, String> response = new HashMap<String, String>();
        String result = accountService.loginSimple(accounts.getUsername(), accounts.getPasswords());
        if (result != null) {
            response = paramServices.messageSuccessApi(200, "success", "Đăng Nhập thành công");
            response.put("token", result);
            return ResponseEntity.ok(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "Đăng Nhập thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
        // Kiểm tra lỗi cho thêm mới
        StatusResponseDto statusResponseDto = errorsServices.errorAccountStaff(accountModel, false);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        AccountDto createdAccount = accountService.AddAccountStaff(accountModel);
        return ResponseEntity.ok(createdAccount);
    }

    @PutMapping("update-account-staff/{id}")
    public ResponseEntity<?> updateAccountStaff(@PathVariable Integer id,
                                                @Valid @RequestBody accountModel accountModel) {
        // Kiểm tra lỗi cho cập nhật
        StatusResponseDto statusResponseDto = errorsServices.errorAccountStaff(accountModel, true);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
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
        System.out.println(email);
        Boolean result = accountService.sendEmailUpdatePassword(email);
        if (result == false) {
            response.put("message", "Email Không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("message", "Email sent successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam("token") String token) {

        boolean flag = accountService.sendPassword(token);
        System.out.println(flag);
        String generateHtmls = paramServices.generateHtml("Thông Báo", "Mật khẩu thành công vừa gửi qua email của bạn",
                "Mời Bạn quay về login để đăng nhập");

        return ResponseEntity.ok(generateHtmls);
    }

    @PutMapping("changepassword")
    public ResponseEntity<?> changepass(@RequestBody changePasswordModel changePasswordModels) {
        Map<String, String> response = new HashMap<String, String>();
        response = accountService.changeUpdatePass(changePasswordModels);
        if (response.get("code").equals(String.valueOf(400))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("account-by-id/{username}")
    public ResponseEntity<?> getAccountById(@PathVariable("username") String username) {
        return ResponseEntity.ok(accountService.getAccountInfoByUsername(username));
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<?> update(@Valid @RequestBody accountModel accountModels) {
        StatusResponseDto statusResponseDto = errorsServices.errorUpdateProfile(accountModels);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        Map<String, String> response = new HashMap<String, String>();
        boolean flag = accountService.updateProfiles(accountModels);
        if (flag) {
            response = paramServices.messageSuccessApi(201, "success", "cập nhật thành công");
            response.put("token", jwtService.generateToken(accountModels.getUsername()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "cập nhật thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public StatusResponseDto delete(@PathVariable Integer id) {
        boolean result = accountService.deleteAccountEmployee(id);
        if (result) {
            return new StatusResponseDto("200", "success", "Xóa nhân viên thành công");
        } else {
            return new StatusResponseDto("400", "error", "Nhân viên này đã xác nhận hóa đơn không thể xóa");
        }
    }

    public boolean updateProfiles(accountModel accountModels) {
        if (accountModels == null) {
            return false;
        }
        Optional<Account> getAccount = accountRepository.findByUsername(accountModels.getUsername());
        if (getAccount.isEmpty()) {
            System.out.println("Tài khoản không tồn tại!");
            return false;
        }
        Account account = getAccount.get();
        System.out.println("Tài khoản được tìm thấy: " + account.getUsername());
        System.out.println("ID được tìm thấy: " + account.getId());

        account.setEmail(accountModels.getEmail());
        account.setFullname(accountModels.getFullname());
        account.setGender(accountModels.getGender());
        account.setPhone(accountModels.getPhone());
        account.setAvatar(accountModels.getAvatar());

        accountRepository.save(account);
        return true;
    }

    public boolean deleteAccountEmployee(Integer id) {
        try {
            Optional<Account> account = accountRepository.findById(id);

            if (account.isEmpty()) {
                return false;
            }

            accountRepository.deleteById(id);
            return true;

        } catch (DataIntegrityViolationException e) {
            System.out.println("Nhân viên này đã xác nhận hóa đơn không thể xóa");
            return false;
        }
    }

    @PutMapping("/updateAccountCustomer")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody accountModel accountModels) {
        StatusResponseDto statusResponseDto = errorsServices.errorUpdateProfile(accountModels);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        Map<String, String> response = new HashMap<String, String>();
        boolean flag = accountService.updateProfileCustomer(accountModels);
        if (flag) {
            response = paramServices.messageSuccessApi(201, "success", "cập nhật thành công");
            response.put("token", jwtService.generateToken(accountModels.getUsername()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response = paramServices.messageSuccessApi(400, "error", "cập nhật thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
