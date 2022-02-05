package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentMem;

import java.util.Collection;

@Service
public class AccidentService {

    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    public Collection<Accident> findAllAccidents() {
        return accidentMem.findAllAccidents();
    }

    public void save(Accident accident) {
       accidentMem.save(accident);
    }

    public Accident findByIdAccident(int id) {
        return accidentMem.findAccidentById(id);
    }
}
