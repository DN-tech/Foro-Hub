package com.AluraLatam.ForoHub.controller;

import com.AluraLatam.ForoHub.domain.autor.Autor;
import com.AluraLatam.ForoHub.domain.autor.AutorRepository;
import com.AluraLatam.ForoHub.domain.autor.DatosRegistroAutor;
import com.AluraLatam.ForoHub.domain.topico.*;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    AutorRepository autorRepository;

    @GetMapping("/hola")
    public String holaMundo() {
        return "Hola Mundo";
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> crearTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder){
        Autor autor = autorRepository.getReferenceById(datosRegistroTopico.idAutor());
        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico, autor));
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicos(@PageableDefault(size = 5) Pageable pageable){

        return ResponseEntity.ok(topicoRepository.findAll(pageable).map(DatosRespuestaTopico::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> buscarTopicoId(@PathVariable long id){
        Topico topico = topicoRepository.getReferenceById(id);

        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable long id ,@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){
        Optional<Topico> topicoP = topicoRepository.findById(id);

        if (topicoP.isPresent()){
            Topico topico = topicoP.get();
            topico.actualizarDatos(datosActualizarTopico);
            return ResponseEntity.ok(new DatosRespuestaTopico(topico));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable long id){
        Optional<Topico> topicoP = topicoRepository.findById(id);
        if (topicoP.isPresent()){
            topicoRepository.deleteById(topicoP.get().getId());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }




}
