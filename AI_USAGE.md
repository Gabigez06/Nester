# Registo de uso de IA

Este documento recoge los usos relevantes de inteligencia artificial realizados durante el proyecto.

## Principios

-La IA puede proponer, pero el equipo decide.
-Todo cambio debe ser comprendido y revisado.
-Las propuestas deben verificarse mediante las herramientas correspondientes.
-No se integrará código generado por IA sin revisión humana.

## Hito 1 - Desarrollo funcional
## Implementación de Inicio de Sesión (Issue #44)

- **Tarea delegada:** Implementación del flujo de inicio de sesión (Controlador, Servicio y vista).
- **Herramienta utilizada:** Asistente de IA (Deepseek).
- **Qué se revisó y adaptó:** Se adaptó el código al estilo del equipo (uso de `PasswordEncoder` inyectado, `IllegalArgumentException` y `RedirectAttributes`). Se solucionó un error 403 (CSRF) añadiendo `th:action` en el formulario.
- **Verificación:** Prueba manual en local y `.\mvnw.cmd clean verify` (BUILD SUCCESS).