package com.wintam.service;

import com.wintam.model.User;

public interface KarmaService {
    void rewardAttendance(User usuario);
    void penalizeHost(User anfitrion);
    void penalizeAttendee(User asistente);
}
