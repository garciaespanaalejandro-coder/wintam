package com.wintam.service;

import com.wintam.dto.AuthResponse;
import com.wintam.dto.LoginRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.dto.RegisterRequest;
import com.wintam.exception.EmailNotVerifiedException;
import com.wintam.exception.UserNotFoundException;
import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import com.wintam.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceSpring implements AuthService{
    private final UserRepository user;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceSpring(UserRepository user,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.user = user;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    //TODO comprobar que varios usuarios no puedan registrarse(gracias javi) con el mismo mail

    @Override
    @Transactional()
    public MessageResponse createAccount(RegisterRequest request) {

        return null;
    }

    @Override
    public AuthResponse signInAccount(LoginRequest request) {
        User user1= user.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(request.getEmail()));
        if(!user1.getIsVerified()){
            throw new EmailNotVerifiedException(user1.getEmail());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtService.generateToken(user1);

        return new AuthResponse(token, user1.getEmail(), user1.getUsername(), user1.getRole().name());
    }
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
