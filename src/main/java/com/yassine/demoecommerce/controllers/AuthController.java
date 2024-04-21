package com.yassine.demoecommerce.controllers;

import com.yassine.demoecommerce.dto.AuthenticationRequest;
import com.yassine.demoecommerce.dto.SingupRequest;
import com.yassine.demoecommerce.dto.UserDto;
import com.yassine.demoecommerce.entity.User;
import com.yassine.demoecommerce.repository.UserRepository;
import com.yassine.demoecommerce.service.auth.AuthService;
import com.yassine.demoecommerce.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

private final AuthenticationManager authenticationManager;
private final UserDetailsService userDetailsService;
@Autowired
private UserRepository userRepository;
private final JwtUtil jwtUtil;
@Autowired
private AuthService authService;
private static final String token_prefix="Bearer ";
    private static final String token_auhtorization="Auhtorization ";
@PostMapping("/authenticate")
    public void CreateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws JSONException, IOException {
    try {
authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.
        getEmail(),
        authenticationRequest.getPassword()));

    }
    catch (BadCredentialsException e){
        throw  new BadCredentialsException("error username or password");
    }
    final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getEmail()
    );
    Optional<User> optionalUser=userRepository.findByEmail(userDetails.getUsername());

    // Génération du token JWT avec la date d'expiration
    final String jwt = jwtUtil.generateToken(userDetails.getUsername());
    // Formatage de la date d'expiration au format "dd/MM/yyyy HH:mm"
    final String expirationDateFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(jwtUtil.extractExpiration(jwt));

    if (optionalUser.isPresent()) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("User ID", optionalUser.get().getId());
        jsonResponse.put("Role", optionalUser.get().getUserRole());
        jsonResponse.put("jwt", jwt);
        // Ajout de la date d'expiration au format spécifié
        jsonResponse.put("expiration_date", expirationDateFormatted);

        response.getWriter().write(jsonResponse.toString());
    }
    // Ajout du token JWT à l'en-tête de la réponse
    response.addHeader(token_prefix, token_auhtorization + jwt);
}
    @PostMapping("/signup")

public ResponseEntity<?> singupRequest(@RequestBody SingupRequest singupRequest) {
    if (authService.hasUserWithEmail(singupRequest.getEmail())) {
        return new ResponseEntity<>("user is aleardy exist", HttpStatus.NOT_ACCEPTABLE);


    }
    UserDto userDto = authService.createUser(singupRequest);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
}

}
