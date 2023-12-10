package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enums.Rol;
import com.egg.eggNews.excepciones.MiException;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }
    
    @Transactional
    public void registrar(MultipartFile archivo, String nombreUsuario, String password,
            String password2) throws MiException {
        
        validar(nombreUsuario, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(true);
        usuario.setSueldoMensual(0);
        
        LocalDate localDate = LocalDate.now();
        Date fechaDeAlta = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        usuario.setFechaDeAlta(fechaDeAlta);
        
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);
    }
    
    public void actualizar(MultipartFile archivo, String id, String nombreUsuario,
            String password, String password2) throws MiException {

        validar(nombreUsuario, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            
            String idImagen = null;
            if (usuario.getImagen() != null) idImagen = usuario.getImagen().getId();

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }
        
    private void validar(String nombreUsuario, String password, String password2) throws MiException {

        if (nombreUsuario.isEmpty() || nombreUsuario == null) 
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        
        if (password.isEmpty() || password == null || password.length() <= 5) 
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        
        if (!password.equals(password2))
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
       
        Usuario usuario = usuarioRepositorio.buscarPorNombre(nombreUsuario);
        
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getNombreUsuario(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
        
    }
    
}
