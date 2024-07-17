package com.AluraLatam.ForoHub.infra.security;

import com.AluraLatam.ForoHub.domain.autor.AutorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Obtener el token del header
        var authHeader = request.getHeader("Authorization");
        if(authHeader != null)
        {
            var token = authHeader.replace("Bearer ", "");
            var subject = tokenService.getSubject(token);
            if (subject != null) {
                //token valido y forzamos inicio de sesión
                var autor = autorRepository.findByCorreo(subject);

                //le decimos que el usuario existe y forzamos un inicio de sesión
                var authentication = new UsernamePasswordAuthenticationToken(autor, null,
                        autor.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);

    }
}
