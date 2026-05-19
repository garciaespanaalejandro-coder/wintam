package com.wintam.service;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.UpdateProfileRequest;
import com.wintam.dto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile();
    void updateProfile(UpdateProfileRequest request);
}
