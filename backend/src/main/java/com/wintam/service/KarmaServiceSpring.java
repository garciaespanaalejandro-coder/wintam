package com.wintam.service;

import com.wintam.model.User;
import com.wintam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class KarmaServiceSpring implements KarmaService{
    private final UserRepository user;
    public KarmaServiceSpring(UserRepository user) {
        this.user = user;
    }

    @Transactional
    @Override
    public void rewardAttendance(User usuario) {
        if (usuario.getKarma()<100){
            usuario.setKarma(usuario.getKarma()+15);
            user.save(usuario);
        }
    }

    @Override
    @Transactional
    public void penalizeHost(User anfitrion) {
        if(anfitrion.getKarma()>20) {
            anfitrion.setKarma(anfitrion.getKarma() - 20);
            user.save(anfitrion);
        }
    }

    @Override
    @Transactional
    public void penalizeAttendee(User asistente) {
        if (asistente.getKarma()>10){
            asistente.setKarma(asistente.getKarma()-10);
            user.save(asistente);
        }
    }
}
