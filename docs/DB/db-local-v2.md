# Script SQLite V2 — BookLog

Script de base de datos para BookLog con autenticación de usuarios, metadatos de lecturas (título, autor, saga) y colecciones personalizadas.

---

## Tabla de usuarios (autenticación)

```sql
-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombreUsuario TEXT NOT NULL,
    correo TEXT NOT NULL UNIQUE,
    hashContrasena TEXT NOT NULL,
    fechaCreacion INTEGER NOT NULL,
    fechaActualizacion INTEGER NOT NULL
);

CREATE UNIQUE INDEX idx_usuarios_nombreUsuario ON usuarios(nombreUsuario);
```

---

## Tabla de sagas/series

```sql
-- Tabla Series (sagas o series a las que puede pertenecer una lectura)
CREATE TABLE IF NOT EXISTS series (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    fechaCreacion INTEGER NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_series_usuarioId ON series(usuarioId);
```

---

## Tabla de colecciones

```sql
-- Tabla Colecciones (categorías: terror, académico, favoritos, etc.)
CREATE TABLE IF NOT EXISTS colecciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    color TEXT,
    fechaCreacion INTEGER NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_colecciones_usuarioId ON colecciones(usuarioId);
```

---

## Tabla principal Readings (actualizada)

```sql
-- Tabla Lecturas
CREATE TABLE IF NOT EXISTS lecturas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId INTEGER NOT NULL,
    rutaArchivo TEXT NOT NULL,
    nombreArchivo TEXT NOT NULL,
    titulo TEXT NOT NULL,
    autor TEXT,
    progreso FLOAT,
    serieId INTEGER,
    estado TEXT CHECK(estado IN ('PENDIENTE', 'EN_PROGRESO', 'FINALIZADA')) DEFAULT 'PENDIENTE',
    fechaCreacion INTEGER NOT NULL,
    fechaActualizacion INTEGER NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (serieId) REFERENCES series(id) ON DELETE SET NULL
);

CREATE INDEX idx_lecturas_usuarioId ON lecturas(usuarioId);
CREATE INDEX idx_lecturas_serieId ON lecturas(serieId);
CREATE INDEX idx_lecturas_estado ON lecturas(estado);
```

---

## Tabla de relación Reading–Collection (muchos a muchos)

```sql
-- Tabla LecturasColecciones (un libro puede estar en varias colecciones)
CREATE TABLE IF NOT EXISTS lecturas_colecciones (
    lecturaId INTEGER NOT NULL,
    coleccionId INTEGER NOT NULL,
    fechaAgregado INTEGER NOT NULL,
    PRIMARY KEY (lecturaId, coleccionId),
    FOREIGN KEY (lecturaId) REFERENCES lecturas(id) ON DELETE CASCADE,
    FOREIGN KEY (coleccionId) REFERENCES colecciones(id) ON DELETE CASCADE
);

CREATE INDEX idx_lecturas_colecciones_lecturaId ON lecturas_colecciones(lecturaId);
CREATE INDEX idx_lecturas_colecciones_coleccionId ON lecturas_colecciones(coleccionId);
```

---

## Tabla Notes

```sql
-- Tabla Notas
CREATE TABLE IF NOT EXISTS notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lecturaId INTEGER NOT NULL,
    contenido TEXT NOT NULL,
    referenciaPagina TEXT,
    fechaCreacion INTEGER NOT NULL,
    FOREIGN KEY (lecturaId) REFERENCES lecturas(id) ON DELETE CASCADE
);

CREATE INDEX idx_notas_lecturaId ON notas(lecturaId);
```

---

## Tabla Quotes

```sql
-- Tabla Citas
CREATE TABLE IF NOT EXISTS citas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lecturaId INTEGER NOT NULL,
    textoCitado TEXT NOT NULL,
    referenciaPagina TEXT,
    comentario TEXT,
    fechaCreacion INTEGER NOT NULL,
    FOREIGN KEY (lecturaId) REFERENCES lecturas(id) ON DELETE CASCADE
);

CREATE INDEX idx_citas_lecturaId ON citas(lecturaId);
```

---

## Resumen de cambios respecto a V1

| Aspecto | V1 | V2 |
|---------|----|----|
| **Autenticación** | No | Tabla `usuarios` con `nombreUsuario` y `hashContrasena` |
| **Título** | Solo `nombreArchivo` | `titulo` editable + `nombreArchivo` (nombre original) |
| **Autor** | No | Campo `autor` opcional |
| **Saga/Serie** | No | Tabla `series` + `serieId` en `lecturas` |
| **Colecciones** | No | Tabla `colecciones` + `lecturas_colecciones` (N:M) |
| **Alcance por usuario** | Global | Todas las entidades asociadas a `usuarioId` |

---

## Notas de implementación

- **Contraseñas**: Guardar siempre el hash (por ejemplo con BCrypt o Argon2), nunca en texto plano.
- **Título por defecto**: Al crear una lectura, se puede inicializar `titulo` con el valor de `nombreArchivo`.
- **Series opcionales**: `serieId` es nullable; si una lectura no pertenece a ninguna saga, queda en `NULL`.
- **Colecciones**: Un mismo libro puede estar en varias colecciones (ej. "Favoritos" y "Terror").
