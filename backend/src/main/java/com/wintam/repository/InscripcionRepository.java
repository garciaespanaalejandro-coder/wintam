package com.wintam.repository;

import com.wintam.model.Cata;
import com.wintam.model.Inscripcion;
import com.wintam.model.InscripcionStatus;
import com.wintam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByCata(Cata cata);
    List<Inscripcion> findByPlayer(User player);
    Optional<Inscripcion> findByCataAndPlayer(Cata cata, User player);
    int countByCataAndStatus(Cata cata, InscripcionStatus status);
}
