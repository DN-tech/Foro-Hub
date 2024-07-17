package com.AluraLatam.ForoHub.domain.topico;

import com.AluraLatam.ForoHub.domain.autor.Autor;
import com.AluraLatam.ForoHub.domain.autor.AutorRepository;
import jakarta.persistence.*;
import jdk.jfr.Relational;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String mensaje;
    private String fechaCreacion;
    private Boolean estado;
    private String curso;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;


    public Topico(DatosRegistroTopico datosRegistroTopico, Autor autor){
        Date fechaActual = new Date();
        String fechaString = fechaActual.toString();

        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.fechaCreacion = fechaString;
        this.estado = true; //true = abierto, false = cerrado
        this.curso = datosRegistroTopico.curso();
        this.autor = autor;
    }


    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico) {
        if (datosActualizarTopico.titulo() != null){
            this.titulo = datosActualizarTopico.titulo();
        }
        if (datosActualizarTopico.mensaje() != null){
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if (datosActualizarTopico.estado() != null){
            this.estado = datosActualizarTopico.estado();
        }
        if (datosActualizarTopico.curso() != null){
            this.curso = datosActualizarTopico.curso();
        }

    }
}
