package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.UserInfoService;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Models.changePasswordModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Service.AccountService;
import com.hotel.hotel_stars.Service.TypeRoomService;
import com.hotel.hotel_stars.utils.paramService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

	@GetMapping("/getAlls")
	public ResponseEntity<?> getAllAccount() {
		System.out.println("Múi giờ hệ thống: " + ZoneId.systemDefault());
		ZonedDateTime vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		System.out.println("Thời gian hiện tại ở Việt Nam: " + vietnamTime);
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("Thời gian hiện tại (LocalDateTime): " + localDateTime);
		System.out.println(paramServices.localDateToInstant(localDateTime));
		return ResponseEntity.ok("ạksfasj");
	}

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
		Map<String, String> response = new HashMap<String, String>();
		System.out.println("password: " + accountModels.getPasswords());
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
		AccountDto createdAccount = accountService.AddAccountStaff(accountModel);
		return ResponseEntity.ok(createdAccount);
	}

	@PutMapping("update-account-staff/{id}")
	public ResponseEntity<?> updateAccountStaff(@PathVariable Integer id,
			@Valid @RequestBody accountModel accountModel) {
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
	public ResponseEntity<?> update(@RequestBody accountModel accountModels) {
		System.out.println(accountModels.getGender() + "  giới tính");
		Map<String, String> response = new HashMap<String, String>();
		System.out.println(accountModels.getUsername());
		boolean flag = accountService.updateProfiles(accountModels);
		if (flag == true) {
			response = paramServices.messageSuccessApi(200, "success", "cập nhật thành công");
			response.put("token", jwtService.generateToken(accountModels.getUsername()));
			return ResponseEntity.ok(response);
		} else {
			response = paramServices.messageSuccessApi(400, "error", "cập nhật thất bại");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
