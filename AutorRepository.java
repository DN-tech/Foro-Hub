package com.AluraLatam.ForoHub.domain.autor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    UserDetails findByCorreo(String username);

}
