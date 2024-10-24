package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Config.UserInfoService;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        if(flag == true){
            response= paramServices.messageSuccessApi(200,"success","Đăng ký thành công");
        }else {
            response=paramServices.messageSuccessApi(400,"error","Đăng ký  thất bại");
        }
        return ResponseEntity.ok(response);
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
}
