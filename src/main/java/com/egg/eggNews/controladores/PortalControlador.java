package com.egg.eggNews.controladores;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

public class PortalControlador {
    
    @GetMapping("/")
    public String index(ModelMap modelo) {
        return "index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}
