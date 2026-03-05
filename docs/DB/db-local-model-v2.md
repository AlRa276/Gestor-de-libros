# Modelo de base de datos V2 — BookLog

Modelo de datos para la persistencia local con Room (SQLite). Incluye autenticación, metadatos de lecturas (título, autor, saga), colecciones y la gestión de notas y citas.

---

## Diagrama entidad-relación

```mermaid
erDiagram
    Usuario ||--o{ Lectura : "posee"
    Usuario ||--o{ Serie : "crea"
    Usuario ||--o{ Coleccion : "crea"
    Serie ||--o{ Lectura : "agrupa"
    Lectura ||--o{ Nota : "tiene"
    Lectura ||--o{ Cita : "tiene"
    Lectura }o--o{ Coleccion : "pertenece a"

    Usuario {
        long id PK "Primary key"
        string nombreUsuario UK "Nombre de usuario (único)"
        string hashContrasena "Hash de contraseña"
        long fechaCreacion "Fecha de registro"
        long fechaActualizacion "Última actualización"
    }

    Serie {
        long id PK "Primary key"
        long usuarioId FK "Propietario"
        string nombre "Nombre de la saga/serie"
        long fechaCreacion "Fecha de creación"
    }

    Coleccion {
        long id PK "Primary key"
        long usuarioId FK "Propietario"
        string nombre "Nombre (terror, académico, favoritos...)"
        string color "Color opcional para la UI"
        long fechaCreacion "Fecha de creación"
    }

    Lectura {
        long id PK "Primary key"
        long usuarioId FK "Propietario"
        string rutaArchivo "Ruta del archivo"
        string nombreArchivo "Nombre original del archivo"
        string titulo "Título editable"
        string autor "Nombre del autor (opcional)"
        long serieId FK "Saga/serie (opcional)"
        string estado "PENDIENTE | EN_PROGRESO | FINALIZADA"
        long fechaCreacion "Fecha de registro"
        long fechaActualizacion "Última actualización"
    }

    LecturaColeccion {
        long lecturaId PK,FK "Lectura"
        long coleccionId PK,FK "Colección"
        long fechaAgregado "Fecha de incorporación"
    }

    Nota {
        long id PK "Primary key"
        long lecturaId FK "Referencia a Lectura"
        string contenido "Contenido de la nota"
        string referenciaPagina "Referencia de página"
        long fechaCreacion "Fecha de creación"
    }

    Cita {
        long id PK "Primary key"
        long lecturaId FK "Referencia a Lectura"
        string textoCitado "Texto citado"
        string referenciaPagina "Referencia de página"
        string comentario "Comentario opcional"
        long fechaCreacion "Fecha de creación"
    }
```

---

## Diagrama simplificado (vista general)

```mermaid
erDiagram
    User ||--o{ Reading : "lecturas"
    User ||--o{ Series : "sagas"
    User ||--o{ Collection : "colecciones"
    Series ||--o{ Reading : "incluye"
    Reading ||--o{ Note : "notas"
    Reading ||--o{ Quote : "citas"
    Reading }o--o{ Collection : "en"
```

---

## Descripción de entidades

| Entidad | Descripción |
|---------|-------------|
| **Usuario** | Usuario de la aplicación. Autenticación con `nombreUsuario` y `hashContrasena`. |
| **Serie** | Saga o serie literaria (ej. "Harry Potter", "El Señor de los Anillos"). Opcional por lectura. |
| **Coleccion** | Colección o categoría creada por el usuario (terror, académico, favoritos, etc.). |
| **Lectura** | Archivo de lectura con metadatos: `titulo` editable, `autor`, saga y `estado`. |
| **LecturaColeccion** | Tabla de unión N:M entre `Lectura` y `Coleccion`. Un libro puede estar en varias colecciones. |
| **Nota** | Nota personal asociada a una lectura, con `referenciaPagina` opcional. |
| **Cita** | Cita textual asociada a una lectura, con `referenciaPagina` y `comentario` opcionales. |

---

## Relaciones

| Relación | Tipo | Descripción |
|----------|------|-------------|
| Usuario → Lectura | 1:N | Un usuario tiene muchas lecturas. |
| Usuario → Serie | 1:N | Un usuario define sus propias sagas. |
| Usuario → Coleccion | 1:N | Un usuario crea sus colecciones. |
| Serie → Lectura | 1:N | Una saga puede incluir varias lecturas. |
| Lectura → Nota | 1:N | Una lectura puede tener muchas notas. |
| Lectura → Cita | 1:N | Una lectura puede tener muchas citas. |
| Lectura ↔ Coleccion | N:M | Una lectura puede estar en varias colecciones; una colección puede contener varias lecturas. |

---

## Campos nuevos o modificados respecto a V1

| Entidad | Campo | Tipo | Descripción |
|---------|-------|------|-------------|
| Lectura | `usuarioId` | FK | Propietario de la lectura. |
| Lectura | `titulo` | string | Título editable (puede diferir del `nombreArchivo`). |
| Lectura | `autor` | string | Nombre del autor (opcional). |
| Lectura | `serieId` | FK (nullable) | Saga o serie a la que pertenece (opcional). |

---

## Notas de implementación (Room)

- **TypeConverters**: Usar para `status` (enum) y fechas (`Date`/`Instant` ↔ `Long`).
- **Índices**: Los definidos en el script mejoran consultas por `userId`, `readingId`, `seriesId` y `collectionId`.
- **Seguridad**: Nunca almacenar contraseñas en texto plano; usar BCrypt o Argon2 para el hash.
- **Cascadas**: Al eliminar un usuario, se eliminan sus lecturas, series, colecciones y relaciones asociadas.
