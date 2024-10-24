package com.hotel.hotel_stars.Config;




import com.hotel.hotel_stars.Entity.Account;
import com.hotel.hotel_stars.Repository.AccountRepository;
import com.hotel.hotel_stars.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private RoleRepository rolesRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> userDetail = repository.findByUsername(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

//    public boolean addUser(Accounts accounts) {
//        try{
//            Optional<Roles> roles=rolesRepository.findById(4);
//            accounts.setPasswords(encoder.encode(accounts.getPasswords()));
//            accounts.setHotel(null);
//            accounts.setRoles(roles.get());
//            accounts.setGender(true);
//            repository.save(accounts);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//
//    }
//
//    public List<User> getAll(){
//        return repository.findAll();
//    }
}
