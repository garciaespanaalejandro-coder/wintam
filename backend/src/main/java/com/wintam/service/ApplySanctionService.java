package com.wintam.service;

import com.wintam.model.Role;
import com.wintam.model.SanctionType;
import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplySanctionService {

    private final KarmaService karma;
    private final EmailService emailService;
    private final UserRepository userRepository;
    public ApplySanctionService(KarmaService karma, EmailService emailService, UserRepository userRepository) {
        this.karma = karma;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void applySanction(SanctionType sanctionType, User reported){
        switch (sanctionType){
            case WARNING -> {
                emailService.sendAdvice(reported.getEmail());
            }
            case KARMA_PENALTY -> karma.penalizeAttendee(reported);
            case BAN ->{
                reported.setRole(Role.BANNED);
                userRepository.save(reported);
            }
        }
    }
}
