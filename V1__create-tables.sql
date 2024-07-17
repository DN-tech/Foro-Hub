
-- Tabla de Autores
CREATE TABLE autores (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         nombre VARCHAR(100) NOT NULL,
                         correo VARCHAR(100) NOT NULL unique,
                         contrasena VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id)
);

-- Tabla de Topicos
CREATE TABLE topicos (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         titulo VARCHAR(100) NOT NULL,
                         mensaje VARCHAR(500) NOT NULL,
                         fecha_creacion VARCHAR(100) NOT NULL,
                         autor_id BIGINT,
                         estado BOOLEAN,
                         curso VARCHAR(100),
                         PRIMARY KEY (id),
                         FOREIGN KEY (autor_id) REFERENCES autores(id)
);
