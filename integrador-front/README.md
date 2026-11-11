# Gestión académica - Frontend del proyecto integrador

React + Vite + Tailwind. Se conecta al backend Spring Boot (puerto 8081), que guarda todo en MySQL.

## Para ejecutarlo
1. En esta carpeta, en la terminal:

       npm install
       npm run dev

2. Abre la direccion que muestre la terminal (normalmente http://localhost:5173).
   Requiere Node 20.19 o superior (22.12 o superior tambien sirve).
3. Para ver los datos, el backend (`AcademicManagementApplication`) debe estar corriendo en el puerto 8081
   con la clase `WebConfig` de CORS.

## Login
Todas las rutas del sistema (/dashboard, /estudiantes, /programas, etc.) exigen sesion. Sin sesion llevan a /login
y, despues de entrar, regresan a la pagina que se queria abrir. El menu lateral muestra el usuario y el boton
"Cerrar sesion". Si la sesion vence (por tiempo o porque el backend responde 401), se vuelve al login.

El modo se elige en el archivo `.env`, con la variable `VITE_AUTH_MODE`:

| Modo | Que hace | Cuando usarlo |
|------|----------|---------------|
| `demo` | El login se valida solo en el navegador con el usuario `admin` / `admin123`. | Probar la pantalla sin el login del backend. No es seguridad real: los datos siguen sin protegerse. |
| `api`  | El login llama a `POST /api/auth/login` del backend, guarda el token y lo envia en cada peticion (`Authorization: Bearer`). | Cuando el backend ya tenga el login (lote 4). |

Para pasar a `api`: aplica el lote 4 del backend, cambia `VITE_AUTH_MODE=api` en `.env` y reinicia `npm run dev`
(Vite solo lee el `.env` al arrancar).

El backend responde con: { token, tokenType, expiresInSeconds, user, roles }.

## Registro con aprobacion y pagina Usuarios
- `/registro`: cualquier persona puede crear una cuenta (usuario, correo y contrasena). La cuenta queda
  `INACTIVE` con rol `PROFESOR`. Mientras no la active un administrador, al intentar entrar se avisa que
  la cuenta aun no esta activa. Nadie puede elegir ser administrador al registrarse.
- `/usuarios` (solo ADMIN): lista las cuentas, avisa cuantas esperan activacion y permite
  Activar / Desactivar, asignar roles y eliminar. El menu "Usuarios" solo aparece para administradores,
  y un profesor que escriba la direccion es devuelto a /estudiantes.
- En modo `demo` las cuentas nuevas viven en el navegador (localStorage, con la contrasena como hash SHA-256).
  El administrador de prueba es `admin` / `admin123`.

IMPORTANTE: ocultar el menu y la ruta protege la pantalla, no los datos. Para que sea seguro de verdad,
el backend debe hacer cumplir los permisos (ver abajo).

## Lo que falta en el backend para el modo api
1. `POST /api/auth/register` (publico: fuera del interceptor, como el login).
   Recibe `{ username, email, password }`, crea el usuario con status `INACTIVE`, le asigna el rol
   `PROFESOR` (creandolo si no existe) y responde 201. Responde 409 si el username o el email ya existen,
   y 400 si no cumple las validaciones (usuario 3-50, correo valido, contrasena 8-72).
2. Restringir al rol `ADMIN` los endpoints `/api/users/**`, `/api/roles/**` y `/api/user-roles/**`
   (responder 403 al resto). Hoy cualquier usuario con sesion puede usarlos.
3. El login ya responde 403 "El usuario esta inactivo" para las cuentas pendientes (lote 4).

La pagina Usuarios ya funciona en modo `api` con los endpoints que existen hoy
(`/api/users`, `/api/roles`, `/api/user-roles`).

## Que incluye
- Portada (`/`) con acceso al sistema y un aviso que indica si el servidor esta conectado.
- **Dashboard** (`/dashboard`, es la pantalla a la que se llega despues de iniciar sesion): cinco tarjetas de
  color (estudiantes, matriculas activas, grupos, promedio de notas y asistencia) que llevan a su pagina, el estado de las
  matriculas y las ultimas matriculas. Es sencillo, sin graficos. Se calcula en el navegador con los datos que
  ya entrega el backend, sin endpoints nuevos.
- Estudiantes, Profesores, Programas, Periodos, Grupos y Matriculas: lista, busqueda, crear, editar y eliminar.
  En Matriculas tambien se puede completar y cancelar.
- **Materias** (de cada programa) y **Materias por grupo** (que materia se dicta en cada grupo y quien la
  dicta; el backend no permite editarlas: se crean o se eliminan).
- **Notas**: lista, crear, editar y eliminar. Se aprueba con 3.0 o mas.
- **Asistencia** (`/asistencia`): se elige la materia del grupo y la fecha de la clase; aparece la lista de los
  estudiantes con matricula activa en ese grupo, todos como "Presente", y se marca Presente, Ausente, Tarde o
  Excusado (con una observacion opcional). Al guardar se registra la clase completa; si ya habia asistencia de
  esa fecha, se actualiza. La pestana "Resumen" muestra por estudiante cuantas veces estuvo presente, ausente,
  tarde o excusado y su porcentaje (presentes + tardes sobre el total de clases; verde desde 80%).
  No se puede elegir una fecha futura.
- El menu esta organizado por secciones con su color e iconos (Resumen, Academico, Personas, Seguimiento y
  Administracion). El titulo de cada pagina toma el icono y el color de su seccion.
- La portada tiene una seccion "Un vistazo" con una imagen del dashboard (`src/assets/dashboard-preview.webp`,
  es una captura con datos de ejemplo; se puede reemplazar por una propia con el mismo nombre).
- Los mensajes de error (404, 409, 400, 401) vienen del backend y se muestran tal cual en pantalla.

## Orden para cargar datos
Programa, Periodo, Grupo, Estudiante y Matricula.
Para registrar notas hace falta ademas: Materia (del mismo programa del grupo), Profesor y asignar la materia
al grupo en "Materias por grupo". El formulario de nueva nota solo ofrece estudiantes con matricula activa o
completada en el grupo de esa materia, porque asi lo exige el backend.

## Requisitos del backend para Asistencia
Necesita el lote 5 del backend (endpoints `/api/attendances`, `/api/attendances/sessions` y
`/api/attendances/summary`) y que exista la tabla `attendances` (con `ddl-auto: update` Hibernate la crea sola).
Si el backend todavia no lo tiene, el resto del sistema funciona y la tarjeta de asistencia del dashboard
muestra "No disponible".

## Donde cambiar cosas
- Colores: `src/index.css`, bloque `@theme`. La paleta actual es **Azul noche** con acentos vivos.
  - Fondo (ambiente): `ink`, `paper`, `mist`, `line`, `deep` (menu, login y franja final) y `tint` / `tint-dark`
    (tinte de la portada, encabezados de tabla y filas). Cambiando solo estos se cambia el fondo del sitio
    sin tocar los botones ni las tarjetas.
  - Acentos: `pine` (color principal: botones, enlaces; tambien marca "activo" y "aprobado"), `harbor`, `straw`,
    `plum` y `rose`. Cada uno tiene version `-dark` (texto sobre fondos claros) y `-light` (texto e iconos
    sobre el menu oscuro).
  - Los colores de cada seccion del menu estan en `src/config/navigation.js`.
  - Los 24 pares principales de texto y fondo cumplen contraste 4.5:1; si cambias colores, conviene
    mantener esa relacion.
  - Fondo de la paleta anterior (verde apagado): `deep #1d3a3e`, `paper #eef3f0`, `tint #347068`.
  - La imagen de la seccion "Un vistazo" (`src/assets/dashboard-preview.webp`) es una captura con los colores
    actuales: si cambias la paleta, conviene reemplazarla.
- URL del backend y modo de login: archivo `.env`.
- Sesion (guardado, vencimiento): `src/config/auth.js`.
- Peticiones y manejo del 401: `src/config/api.js`.
- Login y registro: `src/pages/Login.jsx`, `src/pages/Register.jsx` y `src/services/authService.js`.
- Usuarios (admin): `src/pages/Users.jsx` y `src/services/userService.js`.
- Usuarios del modo demo: `src/services/demoStore.js`.

## Estructura
    src/config      conexion a la API (axios), sesion y formato de fechas
    src/services    una funcion por cada peticion al backend (y el login)
    src/components  piezas reutilizables (formularios, tabla, menu lateral, ruta protegida)
    src/pages       portada, login, registro, dashboard, usuarios y las paginas de administracion
    src/routes      rutas de la aplicacion
