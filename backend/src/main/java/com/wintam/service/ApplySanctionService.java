package com.wintam.service;

import com.wintam.model.SanctionType;
import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class ApplySanctionService {

    private final KarmaService karma;
    private final UserRepository userRepository;
    public ApplySanctionService(KarmaService karma, UserRepository userRepository) {
        this.karma = karma;
        this.userRepository = userRepository;
    }

    public void applySanction(SanctionType sanctionType, User reported){
        switch (sanctionType){
            case WARNING -> {}
            case KARMA_PENALTY -> karma.penalizeAttendee(reported);
            case BAN ->{
                reported.setIsVerified(false);
                userRepository.save(reported);
            }
        }
    }
}
