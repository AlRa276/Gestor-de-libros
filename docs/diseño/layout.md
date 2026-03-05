## Layout general de BookLog

La aplicación se organiza en torno a:

- **Menú lateral de hamburguesa** (izquierda).
- **Pantallas principales**:
  - **LAYOUT‑1**: Lista de libros (BOOK‑CARDS) con buscador.
  - **LAYOUT‑2**: Lista de categorías (LIST‑CARDS) para autores, series, colecciones, etc.
  - **LAYOUT‑3**: Formulario de edición de ficha (título, autor, serie, etiquetas).

---

## Menú de hamburguesa lateral

- **Ubicación**: Lado izquierdo, desplegable.
- **Apertura**: Icono de hamburguesa en la barra superior de la app.
- **Opciones del menú** (en una lista vertical):
  - **Inicio / Todos**
  - **Pendientes**
  - **En progreso**
  - **Finalizados**
  - **Autores**
  - **Series**
  - **Colecciones**
  - **Cerrar sesión**

Cada opción navega a una vista:

- **Inicio/Todos, Pendientes, En progreso, Finalizados** → usan **LAYOUT‑1** (lista de libros filtrada por estado).
- **Autores, Series, Colecciones** → usan **LAYOUT‑2** (lista de categorías).

---

## Componente BOOK‑CARD (tarjeta de libro)

Basado en `flujo-vistas.md` (20‑34).

Estructura de cada tarjeta:

- **Izquierda**:
  - Portada del libro (imagen o placeholder cuadrado/rectangular).

- **Derecha superior**:
  - **Título** del libro (texto principal).

- **Debajo del título**:
  - **Formato del archivo** (ej. PDF, EPUB, MOBI, TXT).

- **Centro**:
  - **Barra de progreso de lectura** (porcentaje leído).

- **Parte inferior (fila de iconos de acción)**:
  - Icono/botón **“Ver notas/citas”**.
  - Icono/botón **“Editar ficha técnica”**.
  - Icono/botón **“Cambiar estado de lectura”** (abre menú: Pendiente, En progreso, Finalizado).

Layout visual aproximado:

- Fila horizontal:
  - Columna izquierda: portada.
  - Columna derecha:
    - Fila: Título.
    - Fila: Formato.
    - Fila: Barra de progreso.
    - Fila: Iconos de acción.

---

## LAYOUT‑1 — Lista de libros

Pantalla principal de libros con menú lateral y buscador.

### Estructura

- **Barra superior (TopAppBar)**:
  - Lado izquierdo: icono de **hamburguesa** para abrir el menú lateral.
  - Centro: título de la vista (por ejemplo: “Inicio”, “Pendientes”, “En progreso”, “Finalizados”).

- **Zona superior de contenido** (debajo de la barra):
  - **Buscador por nombre de libro**:
    - Campo de texto con placeholder “Buscar por título”.
    - Icono de lupa a la izquierda.
    - Botón claro para limpiar texto (opcional).

- **Contenido principal**:
  - Lista vertical (`LazyColumn`) de **BOOK‑CARDS**.
  - Cada BOOK‑CARD representa una lectura (`lectura`) con:
    - Portada.
    - Título.
    - Formato.
    - Progreso.
    - Iconos de acción (notas/citas, editar ficha técnica, cambiar estado).

### Comportamiento

- El menú de hamburguesa se puede abrir en cualquier filtro (Inicio, Pendientes, etc.).
- El buscador filtra por título dentro de la lista actual.
- El cambio de estado (Pendiente / En progreso / Finalizado) se hace desde cada tarjeta.

---

## Componente LIST‑CARD (tarjeta de categoría)

Usado para Autores, Series, Colecciones, etc.

Contenido de cada tarjeta:

- **Izquierda**:
  - **Nombre de la categoría** (ej. nombre del autor, nombre de la serie, nombre de la colección).

- **Al lado del nombre**:
  - El **número de libros** que tiene esa categoría (por ejemplo, “(5)” o un chip “5 libros”).

- **Pegado al lado derecho**:
  - Menú de **3 puntos verticales** (overflow menu) con opciones:
    - **Cambiar nombre**.
    - **Eliminar**.

Distribución:

- Una sola fila horizontal:
  - Inicio de la fila: nombre de la categoría.
  - Junto al nombre: contador de libros.
  - Extremo derecho: icono de menú de 3 puntos.

---

## LAYOUT‑2 — Lista de categorías

Pantalla para listar autores, series, colecciones, etc., reutilizando LIST‑CARDS.

### Estructura

- **Barra superior (TopAppBar)**:
  - Icono de **hamburguesa** a la izquierda.
  - Título según el contexto:
    - “Autores”
    - “Series”
    - “Colecciones”

- **Contenido principal**:
  - Lista vertical de **LIST‑CARDS**.
  - Cada LIST‑CARD muestra:
    - Nombre de la categoría.
    - Número de libros asociados.
    - Menú de 3 puntos con acciones (cambiar nombre, eliminar).

### Comportamiento

- Tocar una LIST‑CARD:
  - Navega a una vista de detalle o a **LAYOUT‑1** filtrado por esa categoría (por ejemplo, todas las lecturas de un autor concreto).
- Menú de 3 puntos:
  - **Cambiar nombre** → abre **LAYOUT‑3** para editar el nombre.
  - **Eliminar** → pide confirmación y elimina la categoría (sin borrar los libros).

---

## LAYOUT‑3 — Formulario de ficha técnica

Pantalla para editar la información principal de una lectura.

### Campos del formulario

- **Título** (`titulo`)
- **Autor** (`autor`)
- **Serie** (`serieId` o selección de una serie de la lista)
- **Etiquetas / Colecciones**:
  - Selector múltiple de colecciones (favoritos, académico, terror, etc.).
  - O chips seleccionables que representan cada colección.

### Estructura

- **Barra superior (TopAppBar)**:
  - Flecha de **volver** (navegar hacia atrás).
  - Título: “Editar ficha técnica”.

- **Contenido**:
  - Formulario vertical (columna) con los campos:
    - Campo de texto para **Título**.
    - Campo de texto para **Autor**.
    - Selector desplegable para **Serie** (o botón “Sin serie”).
    - Sección de **Etiquetas/Colecciones**:
      - Lista de chips/checkboxes con las colecciones existentes.
  - Botones al final:
    - **Guardar cambios** (principal).
    - **Cancelar** (opcional, vuelve sin guardar).

### Comportamiento

- Al entrar desde una BOOK‑CARD, los campos se rellenan con los datos actuales de la lectura.
- Al pulsar **Guardar cambios**:
  - Se validan los campos obligatorios (por ejemplo, Título no vacío).
  - Se actualizan los datos de la lectura en la base de datos.
  - Se vuelve a la pantalla anterior (por ejemplo, LAYOUT‑1 o detalle).

---

## Resumen de navegación entre layouts

- Menú lateral:
  - **Inicio / Todos**, **Pendientes**, **En progreso**, **Finalizados** → **LAYOUT‑1** (lista de BOOK‑CARDS).
  - **Autores**, **Series**, **Colecciones** → **LAYOUT‑2** (lista de LIST‑CARDS).
- Desde una **BOOK‑CARD**:
  - “Ver notas/citas” → navega a pantalla de detalle de notas/citas (futuro layout).
  - “Editar ficha técnica” → **LAYOUT‑3**.
  - “Cambiar estado de lectura” → cambia solo el estado desde la tarjeta.
- Desde una **LIST‑CARD**:
  - Tocar la tarjeta → LAYOUT‑1 filtrado por esa categoría.
  - Opción “Cambiar nombre” → LAYOUT‑3 apropiado para esa categoría.
  - Opción “Eliminar” → elimina la categoría tras confirmación.

