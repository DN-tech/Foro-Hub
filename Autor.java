package com.AluraLatam.ForoHub.domain.autor;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ValueGenerationType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "autores")
public class Autor implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Autor(DatosRegistroAutor datosRegistroAutor){
        this.nombre = datosRegistroAutor.nombre();
        this.correo = datosRegistroAutor.correo();
        this.contrasena = datosRegistroAutor.contrasena();

    }

    public void actualizarDatos(DatosActualizarAutor datosActualizarAutor) {
        if(datosActualizarAutor.nombre() != null){
            this.nombre = datosActualizarAutor.nombre();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
