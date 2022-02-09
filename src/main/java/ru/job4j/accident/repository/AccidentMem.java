package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class AccidentMem {

    private static final AtomicInteger ACCIDENT_ID = new AtomicInteger(4);
    private final Map<Integer, Accident> accidents = new HashMap<>();
    private final Map<Integer, AccidentType> types = new HashMap<>();
    private final Map<Integer, Rule> rules = new HashMap<>();

    private AccidentMem() {
        types.put(1, AccidentType.of(1, "Две машины"));
        types.put(2, AccidentType.of(2, "Машина и человек"));
        types.put(3, AccidentType.of(3, "Машина и велосипед"));
        rules.put(1, Rule.of(1, "Статья 1"));
        rules.put(2, Rule.of(2, "Статья 2"));
        rules.put(3, Rule.of(3, "Статья 3"));
        accidents.put(1, new Accident(1, "Accident1", "Text1", "Address1", types.get(1)));
        accidents.put(2, new Accident(2, "Accident2", "Text2", "Address2", types.get(2)));
        accidents.get(1).addRule(rules.get(1));
        accidents.get(1).addRule(rules.get(2));
        accidents.get(2).addRule(rules.get(1));
        accidents.get(2).addRule(rules.get(2));
        accidents.get(2).addRule(rules.get(3));
    }

    public Collection<Accident> findAllAccidents() {
        return accidents.values();
    }

    public Collection<AccidentType> findAllAccidentsTypes() {
        return types.values();
    }

    public Collection<Rule> findAllAccidentsRules() {
        return rules.values();
    }

    public Set<Rule> findRulesByIds(int[] rIds) {
        return Arrays.stream(rIds)
                .mapToObj(this::findRuleById)
                .collect(Collectors.toSet());
    }

    public Rule findRuleById(int id) {
        return rules.get(id);
    }

    public AccidentType findTypeById(int id) {
        return types.get(id);
    }

    public void save(Accident accident, int[] rIds) {
        if (accident.getId() == 0) {
            accident.setId(ACCIDENT_ID.incrementAndGet());
        }
        accident.setType(findTypeById(accident.getType().getId()));
        accident.setRules(findRulesByIds(rIds));
        accidents.put(accident.getId(), accident);
    }

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }
}
