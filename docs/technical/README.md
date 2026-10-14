# Base tecnológica

## Spring Boot

La aplicación está desarrollada con Spring Boot y organizada en las capas `domain`, `repository`, `service` y `web`. Usa Thymeleaf para las vistas, Spring MVC para la capa web y Spring Data JPA para el acceso a datos. La clase de entrada es `NesterApplication`.

La base de datos configurada para el proyecto es H2. Sus dependencias están declaradas con alcance de ejecución (`runtime`).

## Maven

Maven gestiona las dependencias y la construcción del proyecto mediante el archivo `pom.xml`. El proyecto hereda la configuración de `spring-boot-starter-parent` y utiliza Java 17.

Se incluye el plugin de Spring Boot para ejecutar y empaquetar la aplicación. El repositorio también contiene el *Maven Wrapper* (`mvnw` y `mvnw.cmd`), que permite usar la versión de Maven configurada sin instalar Maven globalmente.

Para iniciar la aplicación:

```bash
./mvnw spring-boot:run
```
# Gestión de usuarios

## Estado actual

Está implementado el registro básico de usuarios como inquilino o propietario.
El inicio de sesión y los permisos según el tipo de usuario están pendientes.

## Modelo

La entidad `User` se guarda en la tabla `users` e incluye nombre, apellidos,
email, teléfono, contraseña y tipo de usuario. Los tipos posibles son
`INQUILINO` y `PROPIETARIO`.

El email es único. Al registrar, se eliminan espacios al principio y al final
y se convierte a minúsculas antes de comprobar si ya existe.

## Flujo de registro

- `GET /registro`: muestra el formulario de registro.
- `POST /registro`: recibe los datos, comprueba si el email ya existe y guarda
  el usuario.

## Capas

- `domain/User.java`: entidad y tipos de usuario.
- `repository/UserRepository.java`: persistencia y consultas por email.
- `service/UserService.java`: normalización del email y lógica de registro.
- `web/UserController.java`: rutas web.
- `templates/registro.html`: formulario.

## Base de datos

Se usa H2 en memoria. La consola está disponible en `/h2-console` mientras
la aplicación está arrancada. Para consultar los usuarios:

```sql
SELECT * FROM USERS;
```