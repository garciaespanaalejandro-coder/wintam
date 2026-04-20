package com.winetam.repository;

import com.winetam.model.Inscripcion;
import com.winetam.model.InscripcionStatus;
import com.winetam.model.Sesion;
import com.winetam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findBySesion(Sesion sesion);
    List<Inscripcion> findByPlayer(User player);
    Optional<Inscripcion> findBySesionAndPlayer(Sesion sesion, User player);
    int countBySesionAndStatus(Sesion sesion, InscripcionStatus status);
}
