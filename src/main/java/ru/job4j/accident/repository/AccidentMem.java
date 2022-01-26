package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {

    private static final AtomicInteger ACCIDENT_ID = new AtomicInteger(4);
    private final Map<Integer, Accident> accidents = new HashMap<>();

    private AccidentMem() {
        accidents.put(1, new Accident(1, "Accident1", "Text1", "Address1"));
        accidents.put(2, new Accident(2, "Accident2", "Text2", "Address2"));
    }

    public Collection<Accident> findAllAccidents() {
        return accidents.values();
    }

    public void save(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(ACCIDENT_ID.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
    }

    public Accident findByIdAccident(int id) {
        return accidents.get(id);
    }
}
