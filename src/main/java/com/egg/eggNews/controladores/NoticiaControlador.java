package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MiException;
import com.egg.eggNews.servicios.NoticiaServicio;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/") 
public class NoticiaControlador {
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @GetMapping("/")
    public String index(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        
        modelo.addAttribute("noticias", noticias);
        
        return "index.html";
    }
    
    @GetMapping("/ver/{id}")
    public String verNoticia(@PathVariable Integer id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        
        return "noticia_ver.html";
    }
    
    @GetMapping("/registrar") //localhost:8080/noticia/registrar
    public String registrar(ModelMap modelo) {
        
        return "noticia_form.html";
    }
        
    @PostMapping("/registro")
    public String registro(
            @RequestParam String titulo,
            @RequestParam String cuerpo,
            ModelMap modelo,
            RedirectAttributes redirectAttributes) {
        
        try {
            
            noticiaServicio.crearNoticia(titulo, cuerpo);
            
            redirectAttributes.addFlashAttribute("exito", "La noticia fue cargada correctamente");
            
            return "redirect:../";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        
        return "noticia_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, String titulo, String cuerpo, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            
            noticiaServicio.modificarNoticia(id, titulo, cuerpo);
            
            redirectAttributes.addFlashAttribute("exito", "La noticia fue cargada correctamente");
            
            return "redirect:../";
        } catch (MiException ex) {
            modelo.put("noticia", noticiaServicio.getOne(id));
            
            modelo.put("error", ex.getMessage());
            
            return "noticia_modificar.html";
        }
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        
        modelo.addAttribute("noticias", noticias);
        
        return "noticias_list.html";
    }
    
    
    
}
