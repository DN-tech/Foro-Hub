package com.AluraLatam.ForoHub.controller;

import com.AluraLatam.ForoHub.domain.autor.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DatosRespuestaAutor> crearAutor(@RequestBody @Valid DatosRegistroAutor datosRegistroAutor,
                                                          UriComponentsBuilder uriComponentsBuilder){
        String contrasenaEncriptada = passwordEncoder.encode(datosRegistroAutor.contrasena());
        Autor autor = new Autor(datosRegistroAutor);
        autor.setContrasena(contrasenaEncriptada);
        autorRepository.save(autor);
        URI url = uriComponentsBuilder.path("/autores/{id}").buildAndExpand(autor.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaAutor(autor));
    }


    @GetMapping
    public ResponseEntity <Page<DatosListaAutor>> listaAutores(@PageableDefault(size = 2) Pageable pageable) {
        return ResponseEntity.ok(autorRepository.findAll(pageable).map(DatosListaAutor::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaAutor> mostrarAutor(@PathVariable long id){
        Autor autor = autorRepository.getReferenceById(id);
        var datosAutor = new DatosRespuestaAutor(autor);

        return ResponseEntity.ok(datosAutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarAutor (@PathVariable Long id){
        Autor autor = autorRepository.getReferenceById(id);
        autorRepository.delete(autor);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    @Transactional
    public  ResponseEntity<DatosRespuestaAutor> actualizarAutor(@RequestBody @Valid DatosActualizarAutor datosActualizarAutor){
        Autor autor= autorRepository.getReferenceById(datosActualizarAutor.id());
        autor.actualizarDatos(datosActualizarAutor);

        return ResponseEntity.ok(new DatosRespuestaAutor(autor));
    }


}
