package com.wintam.controller;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.UpdateProfileRequest;
import com.wintam.dto.UserProfileResponse;
import com.wintam.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getProfile")
    public ResponseEntity<UserProfileResponse> getProfile(){
        return ResponseEntity.ok(userService.getProfile());
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<MessageResponse> updateProfile(@RequestBody UpdateProfileRequest request){
        userService.updateProfile(request);
        return ResponseEntity.ok(new MessageResponse("Perfil actualizado correctamente."));
    }
}
