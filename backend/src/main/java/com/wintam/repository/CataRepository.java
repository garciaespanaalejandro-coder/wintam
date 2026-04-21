package com.wintam.repository;

import com.wintam.model.Cata;
import com.wintam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CataRepository extends JpaRepository<Cata, Long> {
    List<Cata> findByHost(User host);
}
