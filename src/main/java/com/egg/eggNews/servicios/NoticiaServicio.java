package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MiException;
import com.egg.eggNews.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticiaServicio {
    
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Transactional
    public void crearNoticia(Integer id, String titulo, String cuerpo) throws MiException {
        
        validar(id, titulo, cuerpo);
        
        Noticia noticia = new Noticia();
        
        noticia.setId(id);
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        
        
        noticiaRepositorio.save(noticia);
    }
    
    public List<Noticia> listarNoticias() {
        List<Noticia> libros = new ArrayList();
        
        libros = noticiaRepositorio.findAll();
        
        return libros;
    }
    
    public void modificarNoticia(Integer id, String titulo, String cuerpo) throws MiException {
        
        validar(id, titulo, cuerpo);
        
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            
            noticiaRepositorio.save(noticia);
        }       
    }
    
    public Noticia getOne(Integer id){
       return noticiaRepositorio.getOne(id);
    }
    
    private void validar (Integer id, String titulo, String cuerpo) throws MiException {
        if (id == null) 
            throw new MiException("El id no puede ser nulo");
        
        if (titulo.isEmpty() || titulo == null) 
            throw new MiException("El título no puede ser nulo o estar vacío");
        
        if (cuerpo.isEmpty() || cuerpo == null) 
            throw new MiException("El cuerpo no puede ser nulo o estar vacío");
    }
}

