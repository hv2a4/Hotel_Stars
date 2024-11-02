package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.Config.UserInfoService;
import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.RoleDto;
import com.hotel.hotel_stars.DTO.Select.AccountBookingDTO;
import com.hotel.hotel_stars.Config.JwtService;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Role;
import com.hotel.hotel_stars.Exception.CustomValidationException;
import com.hotel.hotel_stars.Models.accountModel;
import com.hotel.hotel_stars.Models.changePasswordModel;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.RoleRepository;
import com.hotel.hotel_stars.Repository.TypeRoomRepository;
import com.hotel.hotel_stars.utils.paramService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.Optional;
import java.util.regex.Pattern;

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
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private AuthenticationManager authenticationManager;

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
                account.getIsDelete(),
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

        // Sử dụng ModelMapper để ánh xạ từ DTO sang Entity
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

        if (accountModels == null) {
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
            accounts.setIsDelete(true);
            accountRepository.save(accounts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public AccountDto toggleIsDeleteStatus(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setIsDelete(!account.getIsDelete());
        Account updated = accountRepository.save(account);
        return convertToDto(updated);
    }

    public List<AccountBookingDTO> getAccountBookings() {
        List<Object[]> result = accountRepository.findAccountBookings();
        List<AccountBookingDTO> accountBookings = new ArrayList<>();
        for (Object[] row : result) {
            String username = (String) row[0];
            String fullname = String.valueOf(row[1]);
            String phoneNumber = (String) row[2];
            String email = (String) row[3];
            String role = (String) row[4];
            String serviceName = (String) row[5];
            LocalDateTime bookingCreationDate = null;
            if (row[6] instanceof Timestamp) {
                bookingCreationDate = ((Timestamp) row[6]).toLocalDateTime(); // Chuyển đổi từ Timestamp sang LocalDateTime
            } else if (row[6] instanceof java.util.Date) {
                bookingCreationDate = ((java.util.Date) row[6]).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            String avt = String.valueOf(row[7]);
            Boolean gender = (Boolean) row[8];
            AccountBookingDTO accountBookingDTO = new AccountBookingDTO(username, fullname, phoneNumber, email, role, serviceName, bookingCreationDate, avt, gender);
            accountBookings.add(accountBookingDTO);
        }
        return accountBookings;
    }
    public String loginSimple(String username,String password){
        try {
            Optional<Account> accounts=accountRepository.findByUsername(username);
            if(!accounts.get().getIsDelete()){
                return null;
            }
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            return jwtService.generateToken(username);
        } catch (Exception e) {
            return null;
        }
    }
    public boolean updateProfiles(accountModel accountModels){
        if(accountModels==null){
            return  false;
        }
        Optional<Account> getAccount=accountRepository.findByUsername(accountModels.getUsername());
        System.out.println("tài khoản được tìm thấy: "+getAccount.get().getUsername());
        System.out.println("id được tìm thấy: "+getAccount.get().getId());
        getAccount.get().setEmail(accountModels.getEmail());
        getAccount.get().setFullname(accountModels.getFullname());
        getAccount.get().setGender(accountModels.getGender());
        getAccount.get().setPhone(accountModels.getPhone());
        getAccount.get().setAvatar(accountModels.getAvatar());
        accountRepository.save(getAccount.get());
        return true;
    }
    public boolean sendPassword(String token){
        String username = jwtService.extractUsername(token);
        String randomPassword = paramServices.generateTemporaryPassword();
        System.out.println(username);
        Optional<Account> accounts = accountRepository.findByUsername(username);
        System.out.println("email được tìm thấy: "+ accounts.get().getEmail());

        String passwords=encoder.encode(randomPassword) ;
        accounts.get().setPasswords(passwords);
        System.out.println("mật khẩu vừa đổi: "+accounts.get().getPasswords());
        try {
            accountRepository.save( accounts.get());
            System.out.println(randomPassword);
            paramServices.sendEmails(accounts.get().getEmail(), "Mật khẩu mới", "Mật Khẩu mới: "+randomPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean isValidUsername(String username) {
        // Quy tắc: ít nhất 6 ký tự, chỉ chứa chữ cái, số, dấu gạch dưới và dấu chấm, không bắt đầu bằng số
        String regex = "^(?!\\d)([a-zA-Z0-9_.]{6,})$"; // Biểu thức chính quy
        return Pattern.matches(regex, username);
    }

    private boolean isValidPhoneNumber(String phone) {
        String regex = "^[0-9]{10,15}$";
        return Pattern.matches(regex, phone);
    }

    public AccountDto AddAccountStaff(accountModel accountModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        if (!isValidUsername(accountModel.getUsername())) {
            errorMessages.add("Tên người dùng không hợp lệ. Tên người dùng phải có ít nhất 6 ký tự và chỉ chứa chữ cái, số, dấu gạch dưới và dấu chấm, không được bắt đầu bằng số.");
        }
        // Kiểm tra xem các trường có giá trị hợp lệ hay không
        if (accountModel.getUsername() == null || accountModel.getUsername().isEmpty()) {
            errorMessages.add("Tên người dùng không được để trống");
        }
        if (accountModel.getEmail() == null || accountModel.getEmail().isEmpty()) {
            errorMessages.add("Email không được để trống");
        }
        if (accountModel.getPhone() == null || accountModel.getPhone().isEmpty()) {
            errorMessages.add("Số điện thoại không được để trống");
        }
        if (accountModel.getPasswords() == null || accountModel.getPasswords().length() < 6) {
            errorMessages.add("Mật khẩu phải có ít nhất 6 ký tự");
        }
        // Kiểm tra xem tên người dùng, email và số điện thoại đã tồn tại hay chưa
        if (accountRepository.existsByUsername(accountModel.getUsername())) {
            errorMessages.add("Tên người dùng đã tồn tại");
        }
        if (accountRepository.existsByEmail(accountModel.getEmail())) {
            errorMessages.add("Email đã tồn tại");
        }
        if (accountRepository.existsByPhone(accountModel.getPhone())) {
            errorMessages.add("Số điện thoại đã tồn tại");
        }
        if (!isValidPhoneNumber(accountModel.getPhone())) {
            errorMessages.add("Số điện thoại không hợp lệ");
        }
        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new CustomValidationException(errorMessages); // Ném ngoại lệ tùy chỉnh
        }

        try {
            // Tạo đối tượng Role và thiết lập ID
            Role role = new Role();
            role.setId(2); // ID cho vai trò nhân viên

            // Tạo đối tượng Account và thiết lập các thuộc tính từ accountModel
            Account account = new Account();
            account.setUsername(accountModel.getUsername());
            account.setFullname(accountModel.getFullname());
            account.setPhone(accountModel.getPhone());
            account.setEmail(accountModel.getEmail());

            // Kiểm tra và mã hóa mật khẩu
            String encodedPassword = encoder.encode(accountModel.getPasswords());
            account.setPasswords(encodedPassword); // Mã hóa mật khẩu
            account.setGender(accountModel.getGender());
            account.setIsDelete(false); // Đánh dấu tài khoản là không bị xóa
            account.setRole(role); // Gán vai trò cho tài khoản

            // Lưu tài khoản vào cơ sở dữ liệu và chuyển đổi sang DTO
            Account savedAccount = accountRepository.save(account);
            return convertToDto(savedAccount); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp tài khoản)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi thêm tài khoản", e);
        }
    }

    public AccountDto UpdateAccountStaff(Integer accountId, accountModel accountModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem tài khoản có tồn tại hay không
        Optional<Account> existingAccountOpt = accountRepository.findById(accountId);
        if (!existingAccountOpt.isPresent()) {
            throw new CustomValidationException(List.of("Tài khoản không tồn tại"));
        }

        Account existingAccount = existingAccountOpt.get();

        // Kiểm tra các trường có giá trị hợp lệ
        if (accountModel.getUsername() == null || accountModel.getUsername().isEmpty()) {
            errorMessages.add("Tên người dùng không được để trống");
        } else if (!isValidUsername(accountModel.getUsername())) {
            errorMessages.add("Tên người dùng không hợp lệ. Tên người dùng phải có ít nhất 6 ký tự và chỉ chứa chữ cái, số, dấu gạch dưới và dấu chấm, không được bắt đầu bằng số.");
        } else if (!existingAccount.getUsername().equals(accountModel.getUsername()) && accountRepository.existsByUsername(accountModel.getUsername())) {
            errorMessages.add("Tên người dùng đã tồn tại");
        }

        if (accountModel.getEmail() == null || accountModel.getEmail().isEmpty()) {
            errorMessages.add("Email không được để trống");
        } else if (!existingAccount.getEmail().equals(accountModel.getEmail()) && accountRepository.existsByEmail(accountModel.getEmail())) {
            errorMessages.add("Email đã tồn tại");
        }

        if (accountModel.getPhone() == null || accountModel.getPhone().isEmpty()) {
            errorMessages.add("Số điện thoại không được để trống");
        } else if (!isValidPhoneNumber(accountModel.getPhone())) {
            errorMessages.add("Số điện thoại không hợp lệ");
        } else if (!existingAccount.getPhone().equals(accountModel.getPhone()) && accountRepository.existsByPhone(accountModel.getPhone())) {
            errorMessages.add("Số điện thoại đã tồn tại");
        }

        // Kiểm tra mật khẩu nếu có thay đổi
        if (accountModel.getPasswords() != null && accountModel.getPasswords().length() < 6) {
            errorMessages.add("Mật khẩu phải có ít nhất 6 ký tự");
        }

        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new CustomValidationException(errorMessages); // Ném ngoại lệ tùy chỉnh
        }

        try {
            // Cập nhật các thuộc tính cho tài khoản
            existingAccount.setUsername(accountModel.getUsername());
            existingAccount.setFullname(accountModel.getFullname());
            existingAccount.setPhone(accountModel.getPhone());
            existingAccount.setEmail(accountModel.getEmail());

            // Kiểm tra và mã hóa mật khẩu nếu có thay đổi
            if (accountModel.getPasswords() != null && !accountModel.getPasswords().isEmpty()) {
                String encodedPassword = encoder.encode(accountModel.getPasswords());
                existingAccount.setPasswords(encodedPassword); // Mã hóa mật khẩu
            }

            existingAccount.setGender(accountModel.getGender());
            existingAccount.setIsDelete(false); // Đảm bảo tài khoản không bị xóa
            // Chúng ta không cần gán lại vai trò vì vai trò không thay đổi trong hàm này

            // Lưu tài khoản đã cập nhật vào cơ sở dữ liệu và chuyển đổi sang DTO
            Account updatedAccount = accountRepository.save(existingAccount);
            return convertToDto(updatedAccount); // Chuyển đổi tài khoản đã lưu sang DTO

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp tài khoản)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật tài khoản", e);
        }
    }

    public void deleteAccountStaff(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new NoSuchElementException("Tài khoản không tồn tại"); // Ném ngoại lệ nếu không tồn tại
        }
        accountRepository.deleteById(id);
    }

    public Boolean sendEmailUpdatePassword(String email){
        Optional<Account> accountsObject = accountRepository.findByEmail(email);
        if (accountsObject.isEmpty()) {
            return false;
        }
        paramServices.sendEmails(accountsObject.get().getEmail(),"Đổi mật khẩu ",
                "Click vào đây: "+"http://localhost:8080/api/account/updatePassword?token=" +  jwtService.generateSimpleToken(email));
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

    public String loginGG(String email){
        System.out.println("tham số vào: "+email);
        Account accounts= new Account();
        List<Account> listAccount=accountRepository.findAll();
        accounts = paramServices.getTokenGG(email);
        email=accounts.getEmail();


        if (email == null) {
            System.out.println("null rôì");
            return null;
        }

        try {
            System.out.println("accoutGmail: "+accounts.getEmail());
            System.out.println("accoutUserName: "+accounts.getUsername());
            Optional<Account> getAccounts = accountRepository.findByUsername(accounts.getUsername());

            if (getAccounts.isPresent()) {
                System.out.println("tài khoản: "+getAccounts.get().getUsername());
                System.out.println("có tài khoản");
                return jwtService.generateToken(accounts.getUsername());
            } else {
                boolean isDuplicate = listAccount.stream()
                        .map(Account::getEmail)
                        .anyMatch(email::equals);
                if(isDuplicate){
                    return null;
                }
                System.out.println("không có tài khoản: "+accounts.getUsername());
                accountRepository.save(accounts);
                return jwtService.generateToken(accounts.getUsername());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
