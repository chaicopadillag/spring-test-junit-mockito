package dev.team.springtestmockito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.team.springtestmockito.models.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    @Query("select c from Cuenta c where c.persona=?1")
    Optional<Cuenta> findByPersona(String persona);

    // List<Cuenta> findAll();
    // Cuenta findById(Long id);
    // void update(Cuenta cuenta);
}
