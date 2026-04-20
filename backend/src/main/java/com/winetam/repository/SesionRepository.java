package com.winetam.repository;

import com.winetam.model.Sesion;
import com.winetam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByHost(User host);
}
