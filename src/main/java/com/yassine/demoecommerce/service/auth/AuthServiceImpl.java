package com.yassine.demoecommerce.service.auth;

import com.yassine.demoecommerce.dto.SingupRequest;
import com.yassine.demoecommerce.dto.UserDto;
import com.yassine.demoecommerce.entity.User;
import com.yassine.demoecommerce.enums.UserRole;
import com.yassine.demoecommerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(SingupRequest singupRequest) {
        User user = new User();
        user.setEmail(singupRequest.getEmail());
        user.setName(singupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(singupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);

        User createdUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setName(createdUser.getName());
        userDto.setEmail(createdUser.getEmail());
        userDto.setUserRole(createdUser.getUserRole());

        return userDto;
    }
    public boolean hasUserWithEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    @PostConstruct
    public void CreateAdmin(){
       User AdminCount=userRepository.findByUserRole (UserRole.ADMIN);
        if(AdminCount==null){
            User user=new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }
}
