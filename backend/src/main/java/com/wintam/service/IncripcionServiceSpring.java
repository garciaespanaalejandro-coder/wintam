package com.wintam.service;

import com.wintam.dto.ConfirmAttendanceRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.exception.CataNotFoundException;
import com.wintam.exception.InvalidCataStatusException;
import com.wintam.exception.UserAlreadyJoinedException;
import com.wintam.exception.UserNotJoinedException;
import com.wintam.model.*;
import com.wintam.repository.CataRepository;
import com.wintam.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class IncripcionServiceSpring implements InscripcionService{
    private final InscripcionRepository inscripcionDAO;
    private final CataRepository cataDAO;

    public IncripcionServiceSpring(InscripcionRepository inscripcionDAO, CataRepository cataDAO) {
        this.inscripcionDAO = inscripcionDAO;
        this.cataDAO = cataDAO;
    }


    @Transactional
    @Override
    public MessageResponse joinCata(Long id) {
        Cata cata = cataDAO.findById(id)
                .orElseThrow(() -> new CataNotFoundException(id));
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(inscripcionDAO.findByCataAndPlayer(cata,usuario).isPresent()){
            throw new UserAlreadyJoinedException();
        }
        if (cata.getStatus() != CataStatus.OPEN) {
            throw new InvalidCataStatusException(cata.getStatus());
        }

        int inscritos = inscripcionDAO.countByCataAndStatus(cata, InscripcionStatus.CONFIRMED);

        if (inscritos==cata.getMaxAttendees()-1){
            cata.setStatus(CataStatus.FULL);
            cataDAO.save(cata);
        }

        Inscripcion inscripcion= Inscripcion.builder()
                .cata(cata)
                .player(usuario)
                .status(InscripcionStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        inscripcionDAO.save(inscripcion);


        return new MessageResponse("Usuario inscrito correctamente.");
    }

    @Override
    @Transactional
    public MessageResponse cancelJoin(Long id) {
        Cata cata = cataDAO.findById(id)
                .orElseThrow(() -> new CataNotFoundException(id));
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Inscripcion inscripcion = inscripcionDAO.findByCataAndPlayer(cata, usuario)
                .orElseThrow(UserNotJoinedException::new);

        inscripcion.setStatus(InscripcionStatus.CANCELLED);
        inscripcionDAO.save(inscripcion);


        if (cata.getStatus()== CataStatus.FULL){
            cata.setStatus(CataStatus.OPEN);
            cataDAO.save(cata);
        }
        LocalDateTime fechaCata = LocalDateTime.of(cata.getScheduleDate(), cata.getScheduledTime());

        long horas = ChronoUnit.HOURS.between(LocalDateTime.now(), fechaCata);

        if (horas<=24){
            //TODO: restar karma al usuario con karmaservice
        }

        return new MessageResponse("Usuario ha cancelado la sesión correctamente.");
    }

    @Override
    public MessageResponse confirmAttendance(ConfirmAttendanceRequest request) {

    }
}

