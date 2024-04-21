package com.yassine.demoecommerce.service.auth;

import com.yassine.demoecommerce.dto.SingupRequest;
import com.yassine.demoecommerce.dto.UserDto;

public interface AuthService {
    UserDto createUser(SingupRequest singupRequest);
    boolean hasUserWithEmail(String email);
}
