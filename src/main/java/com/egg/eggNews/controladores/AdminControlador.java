package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enums.Rol;
import com.egg.eggNews.excepciones.MiException;
import com.egg.eggNews.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, ModelMap modelo) {
        
        List<String> roles = new ArrayList<>();
        for (Rol rol : EnumSet.allOf(Rol.class)) {
            roles.add(rol.name());
        }

        
        modelo.put("usuario", usuarioServicio.getOne(id));
        modelo.put("roles", roles);

        return "rol_modificar";
    }
    
    @PostMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, Rol rol, ModelMap modelo,
            RedirectAttributes redirectAttributes){
        
        try {
            usuarioServicio.cambiarRol(id, rol);
            
            redirectAttributes.addFlashAttribute("exito", "Rol actualizado exitosamente");
            
            return "redirect:/admin/usuarios";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            
            return "rol_modificar";
        }
    }
    
    @GetMapping("/asignarSueldo/{id}")
    public String asignarSueldo(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));

        return "sueldo_modificar";
    }
    
    @PostMapping("/asignarSueldo/{id}")
    public String asignarSueldo(@PathVariable String id, Integer sueldoMensual, ModelMap modelo,
            RedirectAttributes redirectAttributes){
        
        try {
            usuarioServicio.asignarSueldo(id, sueldoMensual);
            
            redirectAttributes.addFlashAttribute("exito", "Sueldo Asignado exitosamente");
            
            return "redirect:/admin/usuarios";
            
        } catch (MiException ex) {
            modelo.put("usuario", usuarioServicio.getOne(id));
            modelo.put("error", ex.getMessage());
            
            return "sueldo_modificar";
        }
    }
    
    @GetMapping("/darBaja/{id}")
    public String darBaja(@PathVariable String id, RedirectAttributes redirectAttributes) {
        
        try {
            usuarioServicio.darBajaUsuario(id);
            redirectAttributes.addFlashAttribute("exito", "Usuario dado de baja correctamente");
            return "redirect:/admin/usuarios";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/usuarios";
        }

    }
}
