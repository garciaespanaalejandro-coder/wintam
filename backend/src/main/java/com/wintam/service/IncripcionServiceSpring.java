package com.wintam.service;

import com.wintam.dto.AttendeeResponse;
import com.wintam.dto.ConfirmAttendanceRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.exception.*;
import com.wintam.model.*;
import com.wintam.repository.CataRepository;
import com.wintam.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncripcionServiceSpring implements InscripcionService{
    private final InscripcionRepository inscripcionDAO;
    private final CataRepository cataDAO;
    private final KarmaService karma;
    public IncripcionServiceSpring(InscripcionRepository inscripcionDAO, CataRepository cataDAO, KarmaService karma) {
        this.inscripcionDAO = inscripcionDAO;
        this.cataDAO = cataDAO;
        this.karma = karma;
    }


    @Override
    public List<AttendeeResponse> getAttendees(Long cataId) {
        List<Inscripcion> inscripciones = inscripcionDAO.findByCataIdAndStatus(cataId, InscripcionStatus.ATTENDED);
        List<AttendeeResponse> result = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {
            AttendeeResponse attendee = new AttendeeResponse(
                    inscripcion.getPlayer().getKarma(),
                    inscripcion.getPlayer().getUsername()
            );
            result.add(attendee);
        }

        return result;
    }

    @Override
    public List<AttendeeResponse> getRegistered(Long cataId) {
        List<Inscripcion> inscripciones = inscripcionDAO.findByCataIdAndStatusIn(
                cataId, List.of(InscripcionStatus.CONFIRMED, InscripcionStatus.ATTENDED)
        );
        List<AttendeeResponse> result = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {
            AttendeeResponse attendee = new AttendeeResponse(
                    inscripcion.getPlayer().getKarma(),
                    inscripcion.getPlayer().getUsername()
            );
            result.add(attendee);
        }

        return result;
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
            karma.penalizeAttendee(usuario);
        }

        return new MessageResponse("Usuario ha cancelado la sesión correctamente.");
    }

    @Override
    @Transactional
    public MessageResponse confirmAttendance(ConfirmAttendanceRequest request) {
        Cata cata = cataDAO.findById(request.getCataId())
                .orElseThrow(() -> new CataNotFoundException(request.getCataId()));
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (cata.getAttendanceCode() == null || !cata.getAttendanceCode().equals(request.getCode())) {
            throw new CodeDontMatchException();
        }
        Inscripcion inscripcion = inscripcionDAO.findByCataAndPlayer(cata, usuario)
                .orElseThrow(UserNotJoinedException::new);
        karma.rewardAttendance(usuario);
        inscripcion.setStatus(InscripcionStatus.ATTENDED);
        inscripcionDAO.save(inscripcion);
        return new MessageResponse("Asistencia confirmada correctamente.");
    }
}

