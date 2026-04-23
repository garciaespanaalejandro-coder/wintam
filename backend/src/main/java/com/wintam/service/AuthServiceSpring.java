package com.wintam.service;

import com.wintam.dto.*;
import com.wintam.exception.*;
import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import com.wintam.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceSpring implements AuthService{
    private final UserRepository user;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    @Autowired
    public AuthServiceSpring(UserRepository user, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService) {
        this.user = user;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public MessageResponse createAccount(RegisterRequest request) {
        String code= getCode();
        if(user.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        if (user.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(request.getUsername());
        }

        User user1=  User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .birthdate(request.getBirthdate())
                .verificationCode(code)
                .build();
        user.save(user1);
        emailService.sendVerificationEmail(request.getEmail(), code);
        return new MessageResponse("Cuenta creada. Revisa tu correo para verificarla.");
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

    @Override
    @Transactional
    public AuthResponse verifyEmail(VerifiyRequest request) {

        User usuario = user.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (usuario.getIsVerified()) {
            throw new AccountAlreadyVerifiedException(request.getEmail());
        }

        if (!usuario.getVerificationCode().equals(request.getCode())) {
            throw new InvalidCodeException();
        }

        usuario.setIsVerified(true);
        usuario.setVerificationCode(null);
        user.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getUsername(), usuario.getRole().name());
    }

    @Override
    @Transactional
    public MessageResponse recoverPassword(RecoverRequest request) {
        User usuario= user.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(request.getEmail()));
        String code= getCode();
        usuario.setVerificationCode(code);
        user.save(usuario);
        emailService.sendRecoverPassword(request.getEmail(),code);
        return new MessageResponse("Te hemos enviado un código a tu correo para recuperar tu contraseña.");
    }

    @Override
    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        User usuario = user.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!usuario.getVerificationCode().equals(request.getCode())){
            throw new InvalidCodeException();
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

        usuario.setVerificationCode(null);
        user.save(usuario);

        return new MessageResponse("¡Contraseña recuperada correctamente!");
    }

    private String getCode(){
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
