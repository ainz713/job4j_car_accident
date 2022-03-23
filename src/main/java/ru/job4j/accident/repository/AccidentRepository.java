package ru.job4j.accident.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accident.model.Accident;

import java.util.Optional;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {
    @Override
    @Query("SELECT DISTINCT a FROM Accident a "
            + "LEFT JOIN FETCH a.rules r "
            + "ORDER BY a.id")
    Iterable<Accident> findAll();

    @Override
    @Query("SELECT DISTINCT a FROM Accident a "
            + "LEFT JOIN FETCH a.rules r "
            + "WHERE a.id = ?1")
    Optional<Accident> findById(Integer integer);
}