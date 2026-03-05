## Base de datos remota V1 — BookLog (MySQL)

Este documento define una versión **remota** de la base de datos de BookLog usando **MySQL**, basada en el modelo local (SQLite/Room) descrito en `db-local-model-v2.md`.

- Motor recomendado: **InnoDB** (para soporte de claves foráneas y transacciones).
- Codificación recomendada: **utf8mb4** (soporte completo de Unicode, incluidos emojis).

---

## Configuración inicial

```sql
CREATE DATABASE IF NOT EXISTS booklog
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE booklog;
```

---

## Tabla `usuarios` (autenticación)

Equivalente remota de la entidad `Usuario`. Maneja el registro e inicio de sesión.

```sql
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombreUsuario VARCHAR(100) NOT NULL,
    correo VARCHAR(200) NOT NULL UNIQUE,
    hashContrasena VARCHAR(255) NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_nombreUsuario (nombreUsuario)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `series` (sagas o series)

Equivalente remoto de la entidad `Serie`. Cada usuario define sus propias sagas/series.

```sql
CREATE TABLE IF NOT EXISTS series (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    usuarioId BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_series_usuarioId (usuarioId),
    CONSTRAINT fk_series_usuario
        FOREIGN KEY (usuarioId) REFERENCES usuarios (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `colecciones` (colecciones/categorías)

Equivalente remoto de la entidad `Coleccion`. Permite crear categorías como terror, académico, favoritos, etc.

```sql
CREATE TABLE IF NOT EXISTS colecciones (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    usuarioId BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    color VARCHAR(20) DEFAULT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_colecciones_usuarioId (usuarioId),
    CONSTRAINT fk_colecciones_usuario
        FOREIGN KEY (usuarioId) REFERENCES usuarios (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `lecturas` (lecturas)

Equivalente remoto de la entidad `Lectura`. Contiene metadatos de los archivos de lectura.

```sql
CREATE TABLE IF NOT EXISTS lecturas (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    usuarioId BIGINT UNSIGNED NOT NULL,
    rutaArchivo TEXT NOT NULL,
    nombreArchivo VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) DEFAULT NULL,
    serieId BIGINT UNSIGNED DEFAULT NULL,
    estado ENUM('PENDIENTE', 'EN_PROGRESO', 'FINALIZADA') NOT NULL DEFAULT 'PENDIENTE',
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_lecturas_usuarioId (usuarioId),
    KEY idx_lecturas_serieId (serieId),
    KEY idx_lecturas_estado (estado),
    CONSTRAINT fk_lecturas_usuario
        FOREIGN KEY (usuarioId) REFERENCES usuarios (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_lecturas_series
        FOREIGN KEY (serieId) REFERENCES series (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `lecturas_colecciones` (relación Lectura–Coleccion, N:M)

Equivalente remoto de la entidad `LecturaColeccion`. Un libro puede estar en varias colecciones.

```sql
CREATE TABLE IF NOT EXISTS lecturas_colecciones (
    lecturaId BIGINT UNSIGNED NOT NULL,
    coleccionId BIGINT UNSIGNED NOT NULL,
    fechaAgregado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (lecturaId, coleccionId),
    KEY idx_lecturas_colecciones_lecturaId (lecturaId),
    KEY idx_lecturas_colecciones_coleccionId (coleccionId),
    CONSTRAINT fk_lecturas_colecciones_lectura
        FOREIGN KEY (lecturaId) REFERENCES lecturas (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_lecturas_colecciones_coleccion
        FOREIGN KEY (coleccionId) REFERENCES colecciones (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `notas` (notas)

Equivalente remoto de la entidad `Nota`. Registra las notas de una lectura, con referencia de página opcional.

```sql
CREATE TABLE IF NOT EXISTS notas (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    lecturaId BIGINT UNSIGNED NOT NULL,
    contenido TEXT NOT NULL,
    referenciaPagina VARCHAR(50) DEFAULT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_notas_lecturaId (lecturaId),
    CONSTRAINT fk_notas_lectura
        FOREIGN KEY (lecturaId) REFERENCES lecturas (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Tabla `citas` (citas textuales)

Equivalente remoto de la entidad `Cita`. Guarda las citas de una lectura, con comentario opcional.

```sql
CREATE TABLE IF NOT EXISTS citas (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    lecturaId BIGINT UNSIGNED NOT NULL,
    textoCitado TEXT NOT NULL,
    referenciaPagina VARCHAR(50) DEFAULT NULL,
    comentario TEXT DEFAULT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_citas_lecturaId (lecturaId),
    CONSTRAINT fk_citas_lectura
        FOREIGN KEY (lecturaId) REFERENCES lecturas (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
```

---

## Diferencias clave entre la versión local (SQLite) y la remota (MySQL)

- **Tipos de datos**:
  - SQLite usa `INTEGER`, `TEXT`, `REAL`, etc.; aquí se usan `BIGINT UNSIGNED`, `VARCHAR`, `TEXT`, `ENUM`, `TIMESTAMP`.
- **Fechas y tiempos**:
  - En SQLite se manejaban como `INTEGER` (timestamp). En MySQL se usan columnas `TIMESTAMP` con valores por defecto.
- **Restricciones e índices**:
  - En MySQL se definen **índices**, **claves únicas** y **claves foráneas** explícitamente con nombres (`fk_*`, `uk_*`, `idx_*`).
- **Motor y charset**:
  - Se especifica `ENGINE = InnoDB` y `CHARSET = utf8mb4` para asegurar integridad referencial y soporte completo de Unicode.

Esta estructura permite que BookLog pueda usarse como una base de datos remota (por ejemplo, detrás de una API REST) manteniendo la misma lógica de entidades que la versión local en SQLite.

