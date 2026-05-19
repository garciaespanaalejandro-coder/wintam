package com.wintam.service;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.UpdateProfileRequest;
import com.wintam.dto.UserProfileResponse;
import com.wintam.exception.UserAlreadyExistsException;
import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceSpring implements UserService {
    private final UserRepository userRepository;

    public UserServiceSpring(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponse getProfile() {
        User usuario= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UserProfileResponse(usuario.getName(),usuario.getUsername(),usuario.getSurname(),usuario.getEmail(),usuario.getDescription(),usuario.getKarma());
    }

    @Override
    public void updateProfile(UpdateProfileRequest request) {
        User usuario= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (request.getUsername() != null &&
                userRepository.existsByUsername(request.getUsername()) &&
                !request.getUsername().equals(usuario.getUsername())) {
            throw new UserAlreadyExistsException("El username ya está en uso");
        }


        Optional.ofNullable(request.getName()).ifPresent(usuario::setName);
        Optional.ofNullable(request.getSurname()).ifPresent(usuario::setSurname);
        Optional.ofNullable(request.getUsername()).ifPresent(usuario::setUsername);
        Optional.ofNullable(request.getDescription()).ifPresent(usuario::setDescription);

        userRepository.save(usuario);
    }
}
