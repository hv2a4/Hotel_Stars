package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.RoleDto;
import com.hotel.hotel_stars.DTO.selectDTO.FindTypeRoomDto;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Role;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Models.changePasswordModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.RoleRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.utils.paramService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AccountService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TypeRoomRepository typeRoomRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    paramService paramServices;
    @Autowired
    JwtService jwtService;

    public AccountDto convertToDto(Account account) {
        RoleDto roleDto = account.getRole() != null ? new RoleDto(
                account.getRole().getId(),
                account.getRole().getRoleName()
        ) : null;

        return new AccountDto(
                account.getId(),
                account.getUsername(),
                account.getFullname(),
                account.getPhone(),
                account.getEmail(),
                account.getAvatar(),
                account.getGender(),
                roleDto // Ánh xạ RoleDto vào AccountDto
        );
    }



    public Account convertToEntity(AccountDto accountDto) {
        // Chuyển đổi RoleDto sang Role
        Role role = null;
        if (accountDto.getRoleDto() != null) {
            role = new Role();
            role.setId(accountDto.getRoleDto().getId());
            // Nếu Role có thêm thuộc tính nào khác, hãy thiết lập ở đây
        }

        Account account = modelMapper.map(accountDto, Account.class);
        account.setRole(role); // Thiết lập Role cho Account
        return account;
    }


    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.
                stream()
                .map(this::convertToDto)
                .toList();
    }


    public boolean addUser(accountModel accountModels) {
        Account accounts = new Account();

        System.out.println("tên người dùng: " + accountModels.getUsername());
        System.out.println("tên: " + accountModels.getFullname());
        System.out.println("phone: " + accountModels.getPhone());
        System.out.println("email: " + accountModels.getEmail());
        System.out.println("password: " + accountModels.getPasswords());
        if (accountModels == null ) {
            System.out.println("fffff");
            return false;
        }
        try {
            Optional<Role> roles = roleRepository.findById(3);
            accounts.setUsername(accountModels.getUsername());
            accounts.setPasswords(encoder.encode(accountModels.getPasswords()));
            accounts.setFullname(accountModels.getFullname());
            accounts.setEmail(accountModels.getEmail());
            accounts.setPhone(accountModels.getPhone());
            accounts.setAvatar("https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F3c7db4be-6f94-4c19-837e-fbfe8848546f?alt=media&token=2aed7a07-6850-4c82-ae7a-5ee1ba606979");
            accounts.setRole(roles.get());
            accounts.setIsDelete(true);
            accounts.setGender(true);
            accountRepository.save(accounts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Boolean sendEmailUpdatePassword(String email){
        Optional<Account> accountsObject = accountRepository.findByEmail(email);
        if (accountsObject.isEmpty()) {
            return false;
        }
        paramServices.sendEmails(accountsObject.get().getEmail(),"Đổi mật khẩu ","Click vào đây: "+"http://localhost:8080/api/account/updatePassword?token=" +  jwtService.generateSimpleToken(email));
        return true;
    }
    public Map<String, String> changeUpdatePass (changePasswordModel changePasswordModels){
        Map<String, String> response = new HashMap<>();
        Optional<Account> accounts=accountRepository.findByUsername(changePasswordModels.getUsername());
        if(!accounts.isPresent()){
            response =paramServices.messageSuccessApi(400,"error","tài khoản này không tồn tại");
        } else if (!encoder.matches(changePasswordModels.getPassword(), accounts.get().getPasswords())) {
            response =paramServices.messageSuccessApi(400,"error","mật khẩu cũ không đúng");
        }else if(!changePasswordModels.getResetPassword().equals(changePasswordModels.getConfirmPassword())){
            response =paramServices.messageSuccessApi(400,"error","mật khẩu và xác nhận mật khẩu không đúng");
        }else {
            try{
                response =paramServices.messageSuccessApi(200,"success","đổi mật khẩu thành công");
                String password=encoder.encode(changePasswordModels.getResetPassword());
                accounts.get().setPasswords(password);
                accountRepository.save(accounts.get());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return  response;
    }
}
