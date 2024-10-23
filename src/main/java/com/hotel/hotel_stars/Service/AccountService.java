package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.AccountDto;
import com.hotel.hotel_stars.DTO.RoleDto;
import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Entity.Role;
import com.hotel.hotel_stars.Repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;

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
}
