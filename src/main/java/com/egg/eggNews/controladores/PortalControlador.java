package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.excepciones.MiException;
import com.egg.eggNews.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index(ModelMap modelo) {
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PERIODISTA', 'ROLE_ADMINISTRADOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMINISTRADOR")) {
            return "redirect:/admin/dashboard";
        }
        
        return "inicio.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombreUsuario,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo,
            MultipartFile archivo,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioServicio.registrar(archivo, nombreUsuario, password, password2);

            redirectAttributes.addFlashAttribute("exito", "Usuario registrado exitosamente");
            
            return "redirect:/login";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);

            return "registro.html";
        }

    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        
        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos");
        }
        
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PERIODISTA', 'ROLE_ADMINISTRADOR')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PERIODISTA', 'ROLE_ADMINISTRADOR')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, 
            @RequestParam String nombreUsuario, @RequestParam String password, 
            @RequestParam String password2, ModelMap modelo) {

        try {
            usuarioServicio.actualizar(archivo, id, nombreUsuario, password, password2);

            modelo.put("exito", "Usuario actualizado correctamente!");

            return "redirect:/inicio";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);

            return "usuario_modificar.html";
        }

    }
}
