package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentJdbcTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentService {

    private final AccidentJdbcTemplate accidentJdbc;

    public AccidentService(AccidentJdbcTemplate accidentJdbc) {
        this.accidentJdbc = accidentJdbc;
    }

    public Collection<Accident> findAllAccidents() {
        return accidentJdbc.findAllAccidents();
    }

    public Collection<AccidentType> findAllAccidentsTypes() {
        return accidentJdbc.findAllAccidentsTypes();
    }

    public Collection<Rule> findAllAccidentsRules() {
        return accidentJdbc.findAllAccidentsRules();
    }

    public void save(Accident accident) {
        accidentJdbc.save(accident);
    }

    public void initAccidentRules(Accident accident, String[] rIds) {
        int[] ids = Arrays.stream(rIds)
                .mapToInt(Integer::parseInt)
                .toArray();
        accidentJdbc.findRulesByIds(ids)
                .forEach(accident::addRule);
    }

    public Optional<Accident> findAccidentById(int id) {
        return accidentJdbc.findAccidentById(id);
    }
}
