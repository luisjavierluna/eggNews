package com.egg.eggNews.entidades;

import com.egg.eggNews.enums.Rol;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Usuario implements Serializable{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String nombreUsuario;
    private String password;
    
    @Temporal(TemporalType.DATE)
    private Date fechaDeAlta;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;
    private Boolean activo;
    
    @OneToMany
    private List<Noticia> misNoticias;
    private Integer sueldoMensual;
}
