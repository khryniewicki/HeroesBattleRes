package pl.com.khryniewicki.heroesbattle.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.com.khryniewicki.heroesbattle.service.RegisterService;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String getMap(Model model) {
        model.addAttribute("mapWithHeroes", RegisterService.mapWithHeroes);
        return "index";
    }
}
