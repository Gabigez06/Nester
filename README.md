# Plataforma de alquiler de viviendas

Proyecto desarrollado para el laboratorio de Ingeniería del Software II (curso 2026-27)

## Equipo

Empresa: Nester

Integrantes:
-Guillermo Risueño da Silva
-Adrian Calatayud García
-Pablo García Puebla
-Gabriel Moreno Navarro

Portavoz:
-Gabriel Gutiérrez Gonzalez

# Objetivo

Desarrollar una plataforma para la gestión de alquiler de viviendas siguiendo un proceso iterativo e incremental de ingeniería del software.

## Estado

Sprint 0 . Preparación y planificación
Sprint 1: 13/10 - Defensa Intermedia -> Desarrollar registros y log in

## Normas

- No hacer push directo a Main.
- Todo cambio relevante tiene trabajo asociado.
- Todo cambio que afecte a Main llega por Pull Request.
- Toda Pull Request relevante debe ser revisada por otra persona.

## Sprint 0

- Creamos todos los .md convenientes como AI_USAGE, AGENTS, README; y añadimos los archivos convenientes a .gitignore.
- Instalamos maven con las dependencias de spring que vamos a usar y la version de java 17 para que sea compatible con el ordenador de todos los integrantes y usamos application.properties para configurar spring y la base de datos h2.
- Creamos las carpetas necesarias dentro de src y las de docs. En src tendremos el codigo del producto y en docs tendremos documentacion de como creamos el proyecto de src.

## Sprint 1

- Creamos a los "Usuarios" dentro de la base de datos y hacemos una forma de que se puedan registrar y loguear dentro de la web.
- Creamos una forma de que los Usuarios propietarios puedan subir inmuebles a la base de datos y puedan acceder a una lista donde estan todos sus inmuebles.