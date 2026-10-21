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

    // Esta era la ruta antigua que borraste (pantalla de inicio)
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Esta es tu nueva ruta para la lista de inmuebles
    @GetMapping("/lista")
    public String listarInmuebles(Model model) {
        model.addAttribute("inmuebles", propertyService.obtenerTodos());
        return "lista";
    }
    @GetMapping("/")
    public String home() {
        return "home";
    }
}