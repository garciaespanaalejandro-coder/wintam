package com.wintam.service;

import com.wintam.dto.UserProfileResponse;
import com.wintam.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceSpring implements UserService {

    @Override
    public UserProfileResponse getProfile() {
        User usuario= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UserProfileResponse(usuario.getName(),usuario.getUsername(),usuario.getSurname(),usuario.getEmail(),usuario.getDescription(),usuario.getKarma());
    }
}
