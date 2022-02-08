package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.service.AccidentService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class AccidentControl {

    private final AccidentService accidentService;

    public AccidentControl(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", accidentService.findAllAccidentsTypes());
        model.addAttribute("rules", accidentService.findAllAccidentsRules());
        return "accident/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        String[] rIds = req.getParameterValues("rIds");
        accidentService.save(accident, rIds);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.findAccidentById(id));
        model.addAttribute("types", accidentService.findAllAccidentsTypes());
        model.addAttribute("rules", accidentService.findAllAccidentsRules());
        return "accident/update";
    }
}
