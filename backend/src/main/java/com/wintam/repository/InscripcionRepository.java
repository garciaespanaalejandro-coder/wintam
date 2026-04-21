package com.wintam.repository;

import com.wintam.model.Cata;
import com.wintam.model.Inscripcion;
import com.wintam.model.InscripcionStatus;
import com.wintam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findBySesion(Cata sesion);
    List<Inscripcion> findByPlayer(User player);
    Optional<Inscripcion> findBySesionAndPlayer(Cata sesion, User player);
    int countBySesionAndStatus(Cata sesion, InscripcionStatus status);
}
