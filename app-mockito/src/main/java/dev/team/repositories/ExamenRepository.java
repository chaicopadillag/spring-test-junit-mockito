package dev.team.repositories;

import dev.team.models.Examen;

import java.util.List;

public interface ExamenRepository {

    Examen guardar(Examen examen);
    List<Examen> findAll();
}
