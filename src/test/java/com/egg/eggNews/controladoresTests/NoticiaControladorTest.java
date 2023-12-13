package com.egg.eggNews.controladoresTests;

import com.egg.eggNews.controladores.NoticiaControlador;
import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.servicios.NoticiaServicio;
import com.egg.eggNews.servicios.UsuarioServicio;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(NoticiaControlador.class)
public class NoticiaControladorTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsuarioServicio usuarioServicio;
    
    @MockBean
    private NoticiaServicio noticiaServicio;
    
    @Test
    public void deberiaListarNoticias() throws Exception {
        // Configuración del Mock
        List<Noticia> noticias = Arrays.asList(
                new Noticia(1, "Noticia 1", "Cuerpo 1", new Usuario()),
                new Noticia(2, "Noticia 2", "Cuerpo 2", new Usuario())
        );
        
        when(noticiaServicio.listarNoticias())
                .thenReturn(noticias);
        
        // Ejecución de la petición
        mockMvc.perform(MockMvcRequestBuilders.get("/noticia/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("noticias", noticias))
                .andExpect(view().name("noticias_list.html"));
    }
}
