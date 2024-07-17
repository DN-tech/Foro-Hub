package com.AluraLatam.ForoHub.domain.autor;

public record DatosListaAutor(
        long id,
        String nombre,
        String email
) {

    public DatosListaAutor(Autor autor){
        this(autor.getId(), autor.getNombre(), autor.getCorreo());
    }


}
