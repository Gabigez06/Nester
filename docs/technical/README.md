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
El servicio elimina los espacios exteriores del nombre, los apellidos y el teléfono, y normaliza el email a minúsculas. También valida el formato básico del email y el teléfono. La contraseña debe tener al menos 8 caracteres y se guarda codificada con BCrypt.

El email es único. Al registrar, se eliminan espacios al principio y al final
y se convierte a minúsculas antes de comprobar si ya existe.

## Flujo de registro

- `GET /registro`: muestra el formulario de registro.
- `POST /registro`: recibe los datos, comprueba si el email ya existe y guarda
  el usuario.
  Si el registro se completa, el usuario se guarda y se redirige a /. Si hay un error de validación o el email ya existe, se vuelve a /registro y se muestra el mensaje correspondiente.

## Capas

- `domain/User.java`: entidad y tipos de usuario.
- `repository/UserRepository.java`: persistencia y consultas por email.
- `service/UserService.java`: normalización del email y lógica de registro, tambien valida los campos, comprueba duplicados y codigica la constraseña.
- `web/UserController.java`: rutas web.
- `templates/registro.html`: formulario.

## Base de datos

Se usa H2 en memoria. La consola está disponible en `/h2-console` mientras
la aplicación está arrancada. Para consultar los usuarios:

```sql
SELECT * FROM USERS;
```
Para conectarse a la consola, se usa la URL jdbc:h2:mem:rentaldb;DB_CLOSE_ON_EXIT=FALSE, el usuario sa y una contraseña vacía. Al detener la aplicación, los datos en memoria se pierden.

# Lista de inmuebles

## Estado actual

Los usuarios con rol `PROPIETARIO` pueden publicar inmuebles.
**Está implementada la funcionalidad de poder ver la lista de todos los alojamientos publicados en el sistema mediante una tabla dinámica.**

## Modelo y persistencia

La entidad `Property` contiene los datos del inmueble (descripción, ubicación, tipo) y una relación (`@ManyToOne`) con el propietario (`User`). La entidad incluye constructores explícitos (incluyendo el vacío requerido por Hibernate) y sus respectivos métodos accesores.
La persistencia de esta entidad está gestionada por `PropertyRepository` extendiendo de `JpaRepository`.

## Flujo y capas

- `GET /lista`: recupera todos los inmuebles de la base de datos y muestra una tabla con sus detalles (descripción, ubicación, tipo y propietario).
- `GET /propiedades/nueva`: muestra el formulario a propietarios.
- `POST /propiedades/nueva`: guarda el inmueble asociado al propietario de la sesión y redirige al listado.
- `domain/Property.java`: entidad del inmueble con atributos y constructores.
- `repository/PropertyRepository.java`: interfaz para el acceso a los datos de la base de datos.
- `service/PropertyService.java`: publicación y consulta de inmuebles (método `obtenerTodos()`).
- `web/HomeController.java`: maneja la ruta raíz (`/`) y la ruta del listado (`/lista`).
- `web/PropertyController.java`: formulario y publicación.
- `templates/publicar.html`: formulario de publicación.
- `templates/lista.html`: listado de inmuebles renderizado con Thymeleaf.