package es.uclm.Nester.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.uclm.Nester.service.PropertyService;

@Controller
public class HomeController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/lista")
    public String listarInmuebles(Model model) {
        // Añade la lista al modelo para que Thymeleaf la lea
        model.addAttribute("inmuebles", propertyService.obtenerTodos());
        return "lista"; // Retorna la vista lista.html
    }
    @GetMapping("/")
    public String home() {
        return "home";
    }
}