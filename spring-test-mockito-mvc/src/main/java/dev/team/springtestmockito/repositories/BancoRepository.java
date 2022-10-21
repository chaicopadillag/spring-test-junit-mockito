package dev.team.springtestmockito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.team.springtestmockito.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {
    // List<Banco> findAll();
    // Banco findById(Long id);
    // void update(Banco banco);

}
