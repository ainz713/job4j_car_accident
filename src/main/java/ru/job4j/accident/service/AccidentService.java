package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentHibernate;
import ru.job4j.accident.repository.AccidentJdbcTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentService {

    private final AccidentHibernate accidentHibernate;

    public AccidentService(AccidentHibernate accidentHibernate) {
        this.accidentHibernate = accidentHibernate;
    }

    public Collection<Accident> findAllAccidents() {
        return accidentHibernate.findAllAccidents();
    }

    public Collection<AccidentType> findAllAccidentsTypes() {
        return accidentHibernate.findAllAccidentsTypes();
    }

    public Collection<Rule> findAllAccidentsRules() {
        return accidentHibernate.findAllAccidentsRules();
    }

    public void save(Accident accident) {
        accidentHibernate.save(accident);
    }

    public void initAccidentRules(Accident accident, String[] rIds) {
        int[] ids = Arrays.stream(rIds)
                .mapToInt(Integer::parseInt)
                .toArray();
        accidentHibernate.findRulesByIds(ids)
                .forEach(accident::addRule);
    }

    public Collection<Accident> findAccidentById(int id) {
        return accidentHibernate.findAccidentById(id);
    }
}
