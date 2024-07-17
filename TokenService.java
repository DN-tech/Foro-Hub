package com.AluraLatam.ForoHub.infra.security;

import com.AluraLatam.ForoHub.domain.autor.Autor;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {


    @Value("${api.security.secret}")
    private String apiSecret;

    public String generarToken(Autor autor){
        try {
            //Es una MALISIMA practica tener cosas sensibles hardcodeadas en el código
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);

            return JWT.create()
                    .withIssuer("ForoHub")
                    .withSubject(autor.getCorreo())//Login dinamicamente
                    .withClaim("id", autor.getId()) //Le agregamos el parametro id
                    .withExpiresAt(generarFechaExpiracion()) //Le damos una expiración
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }

    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

    public String getSubject(String token){
        if(token == null){
            throw new RuntimeException("Token nullo");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); //Validando firma
            verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("ForoHub")
                    // reusable verifier instance
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            System.out.println(exception.toString());
        }

        if (verifier.getSubject() == null){
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }
}
