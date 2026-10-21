# Registo de uso de IA

Este documento recoge los usos relevantes de inteligencia artificial realizados durante el proyecto.

## Principios

-La IA puede proponer, pero el equipo decide.
-Todo cambio debe ser comprendido y revisado.
-Las propuestas deben verificarse mediante las herramientas correspondientes.
-No se integrará código generado por IA sin revisión humana.

## Hito 1 - Desarrollo funcional

## Implementación de Lista de Inmuebles (Issue #50)

- **Tarea delegada:** Implementación de la vista HTML y la conexión con la base de datos para consultar y mostrar la lista de alojamientos publicados (modelo, repositorio, servicio, controlador y vista).
- **Herramienta utilizada:** Asistente de IA (Gemini).
- **Qué se revisó y adaptó:** Se integró la nueva ruta (`/lista`) en el `HomeController` manteniendo intacta la ruta raíz anterior. Se adaptó el modelo `Property` para asegurar la inclusión de los constructores requeridos por Hibernate (vacío y con parámetros) resolviendo errores de duplicidad. Se separó correctamente el `PropertyRepository` en su propia interfaz y se configuró la tabla en Thymeleaf para mostrar la descripción, ubicación, tipo y propietario.
- **Verificación:** Revisión exhaustiva del código generado, corrección de estructura de archivos y prueba manual de la compilación local.

## Implementación de Registro de Usuarios (Issue #29)
- **Tarea delegada:** Implementación del flujo de registro.
- **Herramienta utilizada:** Codex.
- **Qué se revisó y adaptó:** Se revisó que todos los datos necesarios de los usuarios se pida, que se encripte la contraseña, que no se pueda dejar datos vacios/espacios en las casillas y que todos los datos sean válidos.
- **Verificación:** Prueba de diferentes datos que se almacenen en la base de datos.

## Implementación de Inicio de Sesión (Issue #44)

- **Tarea delegada:** Implementación del flujo de inicio de sesión (Controlador, Servicio y vista).
- **Herramienta utilizada:** Asistente de IA (Deepseek).
- **Qué se revisó y adaptó:** Se adaptó el código al estilo del equipo (uso de `PasswordEncoder` inyectado, `IllegalArgumentException` y `RedirectAttributes`). Se solucionó un error 403 (CSRF) añadiendo `th:action` en el formulario.- **Verificación:** Prueba manual en local y `.\mvnw.cmd clean verify` (BUILD SUCCESS).

## Implementación de Registrar Inmuebles (Issue #52)

- **Tarea delegada:** Implementación del flujo para publicar inmuebles como propietario (modelo, servicio, controlador y vista).
- **Herramienta utilizada:** Asistente de IA (Codex).
- **Qué se revisó y adaptó:** Se adaptaron el modelo `Property`, la asociación con el propietario, la comprobación de su rol y el formulario de publicación integrado con el listado.
- **Verificación:** Revisión del código y prueba manual local del formulario y la publicación.