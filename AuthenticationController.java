package com.AluraLatam.ForoHub.controller;

import com.AluraLatam.ForoHub.domain.autor.Autor;
import com.AluraLatam.ForoHub.domain.autor.DatosAutenticacionAutor;
import com.AluraLatam.ForoHub.infra.security.DatosJWTToken;
import com.AluraLatam.ForoHub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {


    //Clase que dispara automaticamente la autenticación
    //Checa igual la clase SecurityConfiguration para inyectar el paquete que necesita Spring
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionAutor datosAutenticacionAutor){

        //Esta es una abstracción de un token que hace referencia a una entidad de usuario
        Authentication authtoken = new UsernamePasswordAuthenticationToken(datosAutenticacionAutor.correo(),
                datosAutenticacionAutor.contrasena());

        //Aquí autentica al usuario que le estamos pasando
        var autorAutenticado = authenticationManager.authenticate(authtoken);
        //Se genera el token con jwt
        var jwtToken = tokenService.generarToken((Autor) autorAutenticado.getPrincipal());

        return ResponseEntity.ok(new DatosJWTToken(jwtToken));
    }

}
