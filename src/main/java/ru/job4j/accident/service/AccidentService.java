package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.*;

import java.util.*;

@Service
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final AccidentTypeRepository accidentTypeRepository;
    private final RuleRepository ruleRepository;

    public AccidentService(AccidentRepository accidentRepository,
                           AccidentTypeRepository accidentTypeRepository,
                           RuleRepository ruleRepository) {
        this.accidentRepository = accidentRepository;
        this.accidentTypeRepository = accidentTypeRepository;
        this.ruleRepository = ruleRepository;
    }

    public Collection<Accident> findAllAccidents() {
        List<Accident> accidents = new ArrayList<>();
        accidentRepository.findAll().forEach(accidents::add);
        return accidents;
    }

    public Collection<AccidentType> findAllAccidentsTypes() {
        List<AccidentType> accidentTypes = new ArrayList<>();
        accidentTypeRepository.findAll().forEach(accidentTypes::add);
        return accidentTypes;
    }

    public Collection<Rule> findAllAccidentsRules() {
        List<Rule> rules = new ArrayList<>();
        ruleRepository.findAll().forEach(rules::add);
        return rules;
    }

    public void save(Accident accident) {
        accidentRepository.save(accident);
    }

    public void initAccidentRules(Accident accident, String[] rIds) {
        Arrays.stream(rIds)
                .mapToInt(Integer::parseInt)
                .mapToObj(id -> ruleRepository.findById(id).get())
                .forEach(accident::addRule);
    }

    public Accident findAccidentById(int id) {
        return accidentRepository.findById(id).get();
    }
}
