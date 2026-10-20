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