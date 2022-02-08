package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentMem;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Stream;

@Service
public class AccidentService {

    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    public Collection<Accident> findAllAccidents() {
        return accidentMem.findAllAccidents();
    }

    public Collection<AccidentType> findAllAccidentsTypes() {
        return accidentMem.findAllAccidentsTypes();
    }

    public Collection<Rule> findAllAccidentsRules() {
        return accidentMem.findAllAccidentsRules();
    }

    public void save(Accident accident, String[] rIds) {
       accidentMem.save(accident, Stream.of(rIds).mapToInt(Integer::parseInt).toArray());
    }

    public Accident findAccidentById(int id) {
        return accidentMem.findAccidentById(id);
    }
}
