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