package com.AluraLatam.ForoHub.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.SQLErrorCodes;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

import static org.aspectj.weaver.bcel.asm.AsmDetector.rootCause;

@RestControllerAdvice
public class TratadorErrores {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entidadNoEncontrada(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    private record DatosErrorValidacion(String campo,
                                        String error
    ){

        public DatosErrorValidacion(FieldError fieldError){
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }


    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> errorIntegridadDatosSql(DataIntegrityViolationException e) {
        String error = "Error de integridad de datos desconocido";
        String tipoError = "DATA INTEGRITY VIOLATION";

        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof SQLException) {
            SQLException sqlException = (SQLException) rootCause;

            int codigoError = sqlException.getErrorCode();

            if (codigoError == 1062) {
                error = "Datos duplicados";
                if (sqlException.getMessage().contains("unique_titulo")) {
                    error = "Ya existe un topico con el mismo titulo";
                } else if (sqlException.getMessage().contains("unique_mensaje")) {
                    error = "Ya existe un topico con el mismo mensaje";
                }
                tipoError = "UNIQUE CONSTRAINT VIOLATION";
            } else if (codigoError == 1452) { // MySQL foreign key violation error code
                error = "Autor no encontrado";
                tipoError = "FOREIGN KEY VIOLATION";
            }

        }
        return ResponseEntity.badRequest().body("{\nError:" + error + "\nTipo de error: " + tipoError + "\n}");
    }
}
