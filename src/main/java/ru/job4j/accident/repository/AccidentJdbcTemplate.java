package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

//@Repository
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Collection<Accident> findAllAccidents() {
        return jdbc.query("select a.id as accident_id, a.name as accident_name, "
                        + "a.text, a.address, a.type_id, t.name as type_name, "
                        + "r.id as rule_id, r.name as rule_name from accident as a "
                        + "left join type t on a.type_id = t.id "
                        + "left join accident_rule ar on a.id = ar.accident_id "
                        + "left join rule r on ar.rule_id = r.id ",
                rs -> {
                    Map<Integer, Accident> accidents = new HashMap<>();
                    while (rs.next()) {
                        int id = rs.getInt("accident_id");
                        var rule = new Rule();
                        rule.setId(rs.getInt("rule_id"));
                        rule.setName(rs.getString("rule_name"));
                        if (accidents.containsKey(id)) {
                            accidents.get(id).addRule(rule);
                            continue;
                        }
                        var accident = new Accident();
                        accident.setId(id);
                        accident.setName(rs.getString("accident_name"));
                        accident.setText(rs.getString("text"));
                        accident.setAddress(rs.getString("address"));
                        var type = new AccidentType();
                        type.setId(rs.getInt("type_id"));
                        type.setName(rs.getString("type_name"));
                        accident.setType(type);
                        accident.addRule(rule);
                        accidents.put(id, accident);
                    }
                    return accidents.values();
                });
    }

    public List<AccidentType> findAllAccidentsTypes() {
        return jdbc.query(
                "select * from type",
                (rs, rowNum) -> {
                    var rule = new AccidentType();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public List<Rule> findAllAccidentsRules() {
        return jdbc.query(
                "select * from rule",
                (rs, rowNum) -> {
                    var rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public Collection<Rule> findRulesByIds(int[] ids) {
        if (ids.length == 0) {
            return List.of();
        }
        String sql = "SELECT * FROM rules WHERE id IN (";
        sql += ",?".repeat(ids.length).replaceFirst(",", "") + ")";
        final Object[] args = Arrays.stream(ids).boxed().toArray();
        return jdbc.query(sql, args,
                (rs, row) ->
                        Rule.of(
                                rs.getInt("id"),
                                rs.getString("name")
                        )
        );
    }

    public Optional<Rule> findRuleById(int id) {
        return jdbc.query("select * from rule where id = ?",
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    var type = new Rule();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return Optional.of(type);
                }, id);
    }

    public Optional<AccidentType> findTypeById(int id) {
        return jdbc.query("select * from type where id = ?",
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    var type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return Optional.of(type);
                }, id);
    }

    public void save(Accident accident) {
        if (accident.getId() == 0) {
            createAccident(accident);
        } else {
            update(accident);
        }
    }

    public boolean deleteAccident(Accident accident) {
        deleteAccidentRules(accident);
        final String sql = "DELETE FROM accident WHERE id = ?";
        final Object[] args = {accident.getId()};
        return jdbc.update(sql, args) > 0;
    }

    private void createAccident(Accident accident) {
        final String sql = "INSERT INTO accident(name, text, address, type_id) "
                + "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement
                    = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, accident.getName());
            statement.setString(2, accident.getText());
            statement.setString(3, accident.getAddress());
            statement.setInt(4, accident.getType().getId());
            return statement;
        }, keyHolder);
        accident.setId((int) keyHolder.getKeys().get("id"));
        createAccidentRules(accident);
    }

    private void update(Accident accident) {
        final String sql = "UPDATE accident "
                + "SET name = ?, text = ?, address = ?, type_id = ? "
                + "WHERE id = ?";
        jdbc.update(
                sql,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId()
        );
        updateAccidentRules(accident);
    }

    private void updateAccidentRules(Accident accident) {
        deleteAccidentRules(accident);
        createAccidentRules(accident);
    }

    private void deleteAccidentRules(Accident accident) {
        final String sql = "DELETE FROM accident_rule WHERE accident_id = ?";
        final Object[] args = {accident.getId()};
        jdbc.update(sql, args);
    }

    private void createAccidentRules(Accident accident) {
        final String sql = "INSERT INTO accident_rule(accident_id, rule_id) "
                + "VALUES (?, ?)";
        accident.getRules()
                .forEach(rule -> jdbc.update(sql, accident.getId(), rule.getId()));
    }

    public Optional<Accident> findAccidentById(int id) {
        Accident accident = jdbc.query("select a.id as accident_id, a.name as accident_name, "
                        + "a.text, a.address, a.type_id, t.name as type_name from accident a "
                        + "left join type t on a.type_id = t.id where a.id = ?",
                rs -> {
                    if (!rs.next()) {
                        return null;
                    }
                    Accident acc = new Accident();
                    acc.setId(rs.getInt("accident_id"));
                    acc.setName(rs.getString("accident_name"));
                    acc.setText(rs.getString("text"));
                    acc.setAddress(rs.getString("address"));
                    var type = new AccidentType();
                    type.setId(rs.getInt("type_id"));
                    type.setName(rs.getString("type_name"));
                    acc.setType(type);
                    return acc;
                }, id);
        if (accident == null) {
            return Optional.empty();
        }
        List<Rule> rules = jdbc.query("select * from accident_rule a "
                        + "left join rule r on a.rule_id = r.id "
                        + "where a.accident_id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, accident.getId());
        for (Rule rule : rules) {
            accident.addRule(rule);
        }
        return Optional.of(accident);
    }
}