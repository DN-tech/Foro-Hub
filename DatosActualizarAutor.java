package com.AluraLatam.ForoHub.domain.autor;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarAutor(
    @NotNull
    long id,
    String nombre
) {
}
