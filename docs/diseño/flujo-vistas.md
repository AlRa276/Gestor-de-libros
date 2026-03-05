

Aquí tienes el desglose del flujo de vistas:

1. Flujo de Acceso (Autenticación)
Este es el "embudo" inicial de la aplicación.

Vista Registro: Captura de datos nuevos. Te redirige a Inicio de Sesión.

Vista Inicio de Sesión: Validación de credenciales. Si no hay cuenta, permite regresar a Registro. Una vez validado, desbloquea el resto de la app.

Cerrar Sesión: Opción en el menú que destruye la sesión actual y te expulsa de vuelta a Inicio de Sesión.

2. El Centro de Control (Navegación y Listados)
Tu aplicación utiliza un Menú de Hamburguesa como eje central para filtrar qué libros se muestran en la pantalla de inicio.

Estructura de la "Card" de Libro (Componente Reutilizable)
Mencionas que todas las tarjetas son iguales, lo cual es excelente para la consistencia:

Izquierda: Portada del libro.

Derecha Superior: Título.

Debajo del Título: Formato del archivo (PDF, EPUB, etc.).

Centro: Barra de progreso de lectura.

Inferior (Iconos de acción):

Ver Notas/Citas.

Editar Ficha Técnica.

Cambiar Estado de Lectura (Pendiente, En Progreso, Finalizado).

Vistas de Categorización
Estas vistas primero muestran una lista de nombres (ej. una lista de autores) y, al seleccionar uno, muestran los libros filtrados:

Autores: Lista de nombres de autores → Libros de ese autor.

Series / Colecciones: Lista de nombres → Libros pertenecientes.

3. Flujo de Lectura y Edición
Este flujo ocurre cuando ya has interactuado con un libro específico de la lista.

Selección de Portada → Vista Lectura: Es el visor del documento (PDF/EPUB).

Acción: Botón para agregar Nota o Cita vinculada a la página actual.

Icono Editar → Vista Ficha Técnica: Formulario para modificar Título, Autor, Serie o añadir Etiquetas/colecciones.

Icono Notas → Vista Notas/Citas: Pantalla dividida en dos apartados:

Apartado Citas: Visualización de fragmentos de texto.

Apartado Notas: Reflexiones del usuario.

Acciones: Crear, editar o eliminar en ambos apartados.


LISTA DE VISTAS:
🔐 Módulo de Acceso
1. Vista de Registro
Contenido: Campos de texto (Nombre, Correo, Contraseña), botón de "Registrar" y un enlace a "Iniciar Sesión".


2. Vista de Inicio de Sesión
Contenido: Campos de correo y contraseña, botón de "Entrar" y opción para ir a "Registro".


🏠 Módulo Principal (Navegación)
3. Vista de Inicio (Dashboard / Biblioteca/ Todos)
Contenido: Título "BookLog", Menú de hamburguesa y una lista de Cards de libros con todos los libros de la biblioteca, y un boton para agregar los libros desde el telefono a la aplicacion.


4. Vistas de Filtrado (Finalizados / En Progreso/ Pendientes)
Contenido: Misma estructura que el Inicio, pero filtrando por el estado correspondiente.


5. Vistas de Categorías (Autores / Series / Colecciones)
Contenido: Una lista inicial de nombres (ej. nombres de autores). Al hacer clic, muestra los libros asociados a esa categoría.

¿Qué hace?: Organiza la biblioteca. Primero consulta los nombres únicos en la tabla y luego filtra los libros de Room que coincidan con la selección.

📖 Módulo de Lectura y Gestión
6. Vista de Lectura
Contenido: Visor del documento (PDF, EPUB, etc.) y un botón flotante o barra de herramientas para "Agregar Nota/Cita".


7. Vista de Ficha Técnica (Edición)
Contenido: Formulario con campos editables: Título, Autor, Serie y un selector de etiquetas (Tags).


8. Vista de Notas y Citas
Contenido: Dos pestañas o apartados diferenciados. Una lista de citas extraídas y otra de notas personales, con botones para crear, editar o eliminar.


📋 Componente Reutilizable: La BookCard (Tarjeta de Libro)
Esta no es una vista completa, sino el elemento que se repite en casi todas las listas:

Visual: Portada (izq), Título (der-arriba), Formato (abajo del título), Barra de progreso (centro).

Acciones: Iconos para ir directamente a Notas/Citas, Ficha Técnica o cambiar el Estado de lectura (ej. de Pendiente a En Progreso).