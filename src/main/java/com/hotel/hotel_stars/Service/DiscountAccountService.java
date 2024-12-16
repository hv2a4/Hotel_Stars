package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Discount;
import com.hotel.hotel_stars.Entity.DiscountAccount;
import com.hotel.hotel_stars.Models.DiscountAccountModels;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.DiscountAccountRepository;
import com.hotel.hotel_stars.Repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountAccountService {
    @Autowired
    private DiscountAccountRepository discountAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DiscountRepository discountRepository;

    public DiscountAccountDto convertToDto(DiscountAccount discountAccount) {
        DiscountAccountDto discountAccountDto = new DiscountAccountDto();
        discountAccountDto.setId(discountAccount.getId());
        discountAccountDto.setStatusDa(discountAccount.getStatusDa());


        Optional<Account> optionalAccount = accountRepository.findById(discountAccount.getAccount().getId());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            AccountDto accountDto = mapToAccountDto(account);
            discountAccountDto.setAccountDto(accountDto);
        } else {
            discountAccountDto.setAccountDto(null); // Hoặc xử lý giá trị khác nếu cần
        }

        Optional<Discount> optionalDiscount = discountRepository.findById(discountAccount.getDiscount().getId());
        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            DiscountDto discountDto = mapToDiscountDto(discount);
            discountAccountDto.setDiscountDto(discountDto);
        } else {
            discountAccountDto.setDiscountDto(null);
        }
        return discountAccountDto;
    }

    public AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setUsername(account.getUsername());
        accountDto.setFullname(account.getFullname());
        accountDto.setPhone(account.getPhone());
        accountDto.setEmail(account.getEmail());
        accountDto.setAvatar(account.getAvatar());
        accountDto.setGender(account.getGender());
        accountDto.setIsDelete(account.getIsDelete());
        if (account.getRole() != null) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(account.getRole().getId());
            roleDto.setRoleName(account.getRole().getRoleName());
            accountDto.setRoleDto(roleDto);
        }
        return accountDto;
    }

    public DiscountDto mapToDiscountDto(Discount discount) {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setId(discount.getId());
        discountDto.setDiscountName(discount.getDiscountName());
        discountDto.setPercent(discount.getPercent());
        discountDto.setStartDate(discount.getStartDate());
        discountDto.setEndDate(discount.getEndDate());
        return discountDto;
    }

    public List<DiscountAccountDto> getDiscountAccountDtoList() {
        List<DiscountAccount> discountAccountList = discountAccountRepository.findAll();
        return discountAccountList.stream().map(this::convertToDto).toList();
    }

    public StatusResponseDto add(DiscountAccountModels discountAccountModel) {
        // Kiểm tra xem discountAccountModel có null không
        if (discountAccountModel == null) {
            return new StatusResponseDto("400", "FAILURE", "Dữ liệu không được để trống.");
        }
        try {
            // Tạo đối tượng DiscountAccount từ DiscountAccountModel
            DiscountAccount discountAccount = new DiscountAccount();
            Optional<Discount> optionalDiscount = discountRepository.findById(discountAccountModel.getDiscount_id());
            Discount discount = optionalDiscount.get();
            discountAccount.setDiscount(discount);
            Optional<Account> optionalAccount = accountRepository.findById(discountAccountModel.getAccount_id());
            Account account = optionalAccount.get();
            discountAccount.setAccount(account);
            discountAccount.setStatusDa(false);
            // Lưu vào cơ sở dữ liệu
            discountAccountRepository.save(discountAccount);
            return new StatusResponseDto("200", "SUCCESS", "Thêm thành công.");
        } catch (Exception e) {
            return new StatusResponseDto("500", "FAILURE", "Lỗi trong quá trình thêm: " + e.getMessage());
        }
    }
}
