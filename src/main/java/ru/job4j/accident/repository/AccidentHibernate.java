package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AccidentHibernate {
    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }

    public <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Collection<Accident> findAllAccidents() {
        return tx(
                session -> session.createQuery("select distinct st from Accident st "
                        + "LEFT JOIN FETCH st.rules r "
                        + "order by st.id", Accident.class)
                        .list());
    }

    public List<AccidentType> findAllAccidentsTypes() {
        return tx(
                session -> session.createQuery("select distinct st from AccidentType st order by st.id",
                        AccidentType.class)
                        .list());
    }

    public List<Rule> findAllAccidentsRules() {
        return tx(
                session -> session.createQuery("select distinct st from Rule st order by st.id", Rule.class)
                        .list());
    }

    public Collection<Rule> findRulesByIds(int[] ids) {
        List<Rule> rsl = new ArrayList<>();
        List<Integer> idsList = Arrays.stream(ids).boxed().collect(Collectors.toList());
        return tx(
                session -> session.createQuery(
                        "select distinct st from Rule st "
                                + "where st.id in :sId", Rule.class).setParameter("sId", idsList).list()
        );
    }

    public List<Rule> findRuleById(int id) {
        return tx(
                session -> session.createQuery(
                        "select distinct st from Rule st "
                                + "where st.id = :sId", Rule.class).setParameter("sId", id).list()
        );
    }

    public List<AccidentType> findTypeById(int id) {
        return tx(
                session -> session.createQuery(
                        "select distinct st from AccidentType st "
                                + "where st.id = :sId", AccidentType.class).setParameter("sId", id).list()
        );
    }

    public boolean save(Accident accident) {
        return tx(session -> {
            if (accident.getId() == 0) {
                session.save(accident);
            } else {
                session.update(accident);
            }
            return true;
        });
    }

    public boolean delete(Accident accident) {
        return tx(session -> {
            session.delete(session.get(Accident.class, accident.getId()));
           return true;
        });
    }

    public List<Accident> findAccidentById(int id) {
        return tx(
                session -> session.createQuery(
                        "select distinct st from Accident st "
                                + "LEFT JOIN FETCH st.rules r "
                                + "where st.id = :sId", Accident.class).setParameter("sId", id).list()
        );
    }
}
