## Historias de usuario — BookLog

### Autenticación (Users)

- **AU-01 — Registro de usuario**
  - Como usuario nuevo, quiero registrarme con nombre de usuario y contraseña para poder usar la aplicación.
  - Criterios de aceptación:
    - El nombre de usuario debe ser único.
    - La contraseña se almacena como hash, no en texto plano.
    - Se valida que ambos campos no estén vacíos.

- **AU-02 — Inicio de sesión**
  - Como usuario registrado, quiero iniciar sesión con mi nombre de usuario y contraseña para acceder a mis lecturas.
  - Criterios de aceptación:
    - Si las credenciales son correctas, la sesión se inicia y se muestra la pantalla principal.
    - Si las credenciales son incorrectas, se muestra un mensaje de error sin revelar detalles sensibles.

- **AU-03 — Cierre de sesión**
  - Como usuario con sesión iniciada, quiero cerrar sesión para proteger mi información en dispositivos compartidos.
  - Criterios de aceptación:
    - Al cerrar sesión, se limpia el estado de usuario autenticado.
    - Se redirige a la pantalla de inicio de sesión o bienvenida.

---

### Lecturas (Readings)

- **LE-01 — Añadir archivo de lectura**
  - Como usuario, quiero seleccionar archivos de lectura desde mi dispositivo para añadirlos a mi biblioteca.
  - Criterios de aceptación:
    - Se abre un selector de archivos del sistema.
    - Al elegir un archivo válido, se crea un registro en la base de datos con `filePath`, `fileName`, `title` inicializado con el nombre del archivo y estado `PENDIENTE`.

- **LE-02 — Listado de lecturas**
  - Como usuario, quiero ver el listado de mis lecturas registradas para revisar mi historial.
  - Criterios de aceptación:
    - Se muestra una lista con título, autor (si existe) y estado de cada lectura.
    - La lista está ordenada por fecha de creación o última actualización.

- **LE-03 — Cambiar título de lectura**
  - Como usuario, quiero cambiar el título de una lectura para identificarla mejor.
  - Criterios de aceptación:
    - Puedo editar el campo `title` de una lectura.
    - El nuevo título se guarda y se refleja en el listado y en el detalle.

- **LE-04 — Añadir autor a la lectura** 
  - Como usuario, quiero añadir el nombre del autor a una lectura para organizarla mejor.
  - Criterios de aceptación:
    - Puedo establecer o editar el campo `author`.
    - El nombre del autor se muestra en el detalle de la lectura y en la lista.

- **LE-05 — Cambiar estado de lectura**
  - Como usuario, quiero clasificar cada lectura como pendiente, en progreso o finalizada para saber en qué estado está.
  - Criterios de aceptación:
    - Puedo cambiar el campo `status` entre `PENDIENTE`, `EN_PROGRESO` y `FINALIZADA`.
    - El estado se refleja visualmente (por ejemplo, con etiquetas o colores).

- **LE-06 — Asociar lectura a saga o serie** --> Etapa 3
  - Como usuario, quiero indicar la saga o serie a la que pertenece una lectura (si aplica) para agruparla con otras.
  - Criterios de aceptación:
    - Puedo seleccionar una saga/serie existente o dejar el campo vacío.
    - La lectura queda asociada a `seriesId` y es posible ver todas las lecturas de una saga.

- **LE-07 — Añadir lectura a colecciones** --> Etapa 3
  - Como usuario, quiero añadir lecturas a colecciones (terror, académico, favoritos, etc.) para organizarlas por categoría.
  - Criterios de aceptación:
    - Puedo seleccionar una o varias colecciones para una lectura.
    - La relación se guarda en la tabla de unión y se refleja en el detalle de la lectura.

- **LE-08 — Ver detalle de una lectura**
  - Como usuario, quiero ver el detalle de una lectura (metadatos, notas y citas) para revisar su información.
  - Criterios de aceptación:
    - Se muestran título, autor, saga, estado, colecciones asociadas, notas y citas.
    

- **LE-09 — Eliminar lectura**
  - Como usuario, quiero eliminar una lectura de mi biblioteca cuando ya no la necesite.
  - Criterios de aceptación:
    - Se solicita confirmación antes de eliminar.
    - Al eliminar la lectura, se eliminan también sus notas y citas asociadas.

---

### Notas (Notes)

- **NO-01 — Crear nota**
  - Como usuario, quiero crear notas asociadas a una lectura para registrar mis reflexiones.
  - Criterios de aceptación:
    - La nota tiene contenido obligatorio y referencia a una lectura (`readingId`).
    - Puede incluir una referencia de página opcional (`pageReference`).

- **NO-02 — Ver notas de una lectura**
  - Como usuario, quiero ver todas las notas de una lectura para revisar mis reflexiones.
  - Criterios de aceptación:
    - Se muestra la lista de notas filtrada por lectura.
    - Cada nota muestra su contenido y referencia de página, si existe.

- **NO-03 — Editar nota**
  - Como usuario, quiero editar una nota existente para corregir o ampliar su contenido.
  - Criterios de aceptación:
    - Puedo modificar el contenido y la referencia de página.
    - Los cambios se guardan y se actualiza la vista.

- **NO-04 — Eliminar nota**
  - Como usuario, quiero eliminar una nota cuando ya no la necesite.
  - Criterios de aceptación:
    - Se solicita confirmación antes de eliminar.
    - La nota desaparece de la lista y de la base de datos.

---

### Citas textuales (Quotes) 

- **CI-01 — Crear cita textual**
  - Como usuario, quiero crear citas textuales de una lectura para guardar fragmentos relevantes.
  - Criterios de aceptación:
    - La cita tiene texto obligatorio (`quotedText`) y referencia a una lectura.
    - Puede incluir referencia de página (`pageReference`) y comentario opcional (`comment`).

- **CI-02 — Ver citas de una lectura**
  - Como usuario, quiero ver todas las citas de una lectura para revisar mis fragmentos guardados.
  - Criterios de aceptación:
    - Se muestra la lista de citas filtrada por lectura.
    - Cada cita muestra el texto citado, la página (si existe) y el comentario.

- **CI-03 — Editar cita**
  - Como usuario, quiero editar una cita existente para corregir el texto o su comentario.
  - Criterios de aceptación:
    - Puedo modificar `quotedText`, `pageReference` y `comment`.
    - Los cambios se guardan correctamente.

- **CI-04 — Eliminar cita**
  - Como usuario, quiero eliminar una cita cuando ya no la necesite.
  - Criterios de aceptación:
    - Se solicita confirmación antes de eliminar.
    - La cita desaparece de la lista y de la base de datos.

---

### Colecciones (Collections) --> Etapa3

- **CO-01 — Crear colección**
  - Como usuario, quiero crear colecciones (terror, académico, favoritos, etc.) para organizar mis lecturas.
  - Criterios de aceptación:
    - Puedo asignar un nombre obligatorio y un color opcional.
    - La colección queda asociada a mi usuario.

- **CO-02 — Ver colecciones**
  - Como usuario, quiero ver la lista de mis colecciones para navegar por ellas.
  - Criterios de aceptación:
    - Se muestra la lista de colecciones con su nombre y, si existe, su color.

- **CO-03 — Ver lecturas de una colección**
  - Como usuario, quiero ver las lecturas que pertenecen a una colección para filtrar por categoría.
  - Criterios de aceptación:
    - Se listan solo las lecturas asociadas a la colección seleccionada.

- **CO-04 — Quitar lectura de una colección**
  - Como usuario, quiero quitar una lectura de una colección cuando ya no aplique.
  - Criterios de aceptación:
    - Se elimina solo la relación, no la lectura completa.

- **CO-05 — Editar colección**
  - Como usuario, quiero editar el nombre o color de una colección para mantenerla organizada.
  - Criterios de aceptación:
    - Los cambios de nombre y color se guardan y se reflejan en la UI.

- **CO-06 — Eliminar colección**
  - Como usuario, quiero eliminar una colección cuando ya no la use.
  - Criterios de aceptación:
    - Se solicita confirmación antes de eliminar.
    - Se eliminan las relaciones con lecturas, pero las lecturas permanecen en la biblioteca.

---

### Sagas o series (Series) --> Etapa 3 opcional

- **SA-01 — Crear saga o serie**
  - Como usuario, quiero crear sagas o series (por ejemplo, "Harry Potter") para agrupar lecturas relacionadas.
  - Criterios de aceptación:
    - Puedo registrar un nombre para la saga/serie.
    - La saga queda asociada a mi usuario.

- **SA-02 — Asignar lectura a saga**
  - Como usuario, quiero asignar una lectura a una saga existente para mantenerlas agrupadas.
  - Criterios de aceptación:
    - Puedo seleccionar una saga para la lectura o dejarla sin saga.
    - La relación se guarda mediante el campo `seriesId`.

- **SA-03 — Ver lecturas de una saga**
  - Como usuario, quiero ver todas las lecturas de una saga para seguir el orden o el conjunto.
  - Criterios de aceptación:
    - Se muestran todas las lecturas que comparten la misma saga.

- **SA-04 — Quitar lectura de una saga**
  - Como usuario, quiero quitar una lectura de una saga si me equivoqué o ya no corresponde.
  - Criterios de aceptación:
    - Puedo dejar el campo `seriesId` en vacío (sin saga) para esa lectura.

