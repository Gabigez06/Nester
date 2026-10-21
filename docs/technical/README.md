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
Está implementado el inicio de sesión para un usuario ya creado.
Está implementada la funcionalidad de publicar propiedades solo en el caso de estar registrado como propietario.
Está implementada la funcionalidad de poder ver la lista de propiedades publicadas por el usuario en caso de ser propietario.

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

# Gestión de inicio de sesión

## Estado actual
El registro básico de usuarios como inquilino o propietario ya está implementado.
En esta iteración se ha implementado el **inicio de sesión (Login)**. Los permisos específicos según el tipo de usuario (que un Inquilino vea cosas distintas a un Propietario) siguen estando pendientes.

## Modelo
Se reutiliza la entidad `User`, que se guarda en la tabla `users` e incluye nombre, apellidos, email, teléfono, contraseña y tipo de usuario. Los tipos posibles son INQUILINO y PROPIETARIO.
Para el login, se utiliza el `email` (que es único) y la `password` (que está cifrada con BCrypt).

## Flujo de inicio de sesión
- **GET /login**: muestra el formulario de inicio de sesión.
- **POST /login**: recibe los datos (email y contraseña), comprueba si el email existe y valida la contraseña.
  - Si el login es correcto, se guarda el objeto `User` en la sesión (`HttpSession`) bajo la clave `usuarioLogueado` y se redirige a `/`.
  - Si hay un error (el email no existe o la contraseña no coincide), se vuelve a `/login` y se muestra el mensaje correspondiente de forma controlada.

## Capas
- `domain/User.java`: entidad y tipos de usuario.
- `repository/UserRepository.java`: persistencia y consultas por email (método `findByEmail`).
- `service/UserService.java`: lógica de inicio de sesión. Normaliza el email, busca al usuario, valida la contraseña usando el `PasswordEncoder` (BCrypt) y lanza una excepción controlada si falla.
- `web/UserController.java`: rutas web (`GET /login` y `POST /login`).
- `templates/login.html`: formulario de inicio de sesión (con protección CSRF usando `th:action`).
- `config/SecurityConfig.java`: configuración de seguridad y definición del bean `BCryptPasswordEncoder`.

## Base de datos
Se usa H2 en memoria. La consola está disponible en `/h2-console` mientras la aplicación está arrancada. Para consultar los usuarios (y verificar que el login busca correctamente):

`SELECT * FROM USERS;`

Para conectarse a la consola, se usa la URL `jdbc:h2:mem:rentaldb;DB_CLOSE_ON_EXIT=FALSE`, el usuario `sa` y una contraseña vacía. Al detener la aplicación, los datos en memoria se pierden.

# Gestión de inmuebles

## Estado actual

Los usuarios con rol `PROPIETARIO` pueden publicar inmuebles y consultar el listado.

## Modelo y persistencia

La entidad `Property` contiene los datos del inmueble y una relación con el propietario (`User`). Los complementos se guardan en la tabla `property_complements`.

## Flujo y capas

- `GET /propiedades/nueva`: muestra el formulario a propietarios.
- `POST /propiedades/nueva`: guarda el inmueble asociado al propietario de la sesión y redirige al listado.
- `GET /lista`: muestra los inmuebles publicados.
- `domain/Property.java`: entidad del inmueble.
- `repository/PropertyRepository.java`: acceso a los datos.
- `service/PropertyService.java`: publicación y consulta de inmuebles.
- `web/PropertyController.java`: formulario y publicación.
- `templates/publicar.html`: formulario de publicación.
- `templates/lista.html`: listado de inmuebles.