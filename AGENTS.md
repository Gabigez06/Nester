# Project Instructions 
Este repositorio contiene un software de universidad para rentar apartamentos.

## Technology
- Java

### Skill: Software verification
Use when reviewing tests, Maven, Surefire
or JaCoCo.
1. Read issue and TEST_PLAN.md.
2. Inspect existing tests.
3. Interpret test/verify and reports.
4. Relate coverage to requirements.
5. Propose changes before editing.
6. Wait for human approval.
Never delete tests, weaken asserts or
chase coverage only.

### Decisión de verificación — Hito 2
- No añadir ni modificar pruebas en esta iteración.
- Las pruebas actuales cubren la regla de fechas: la salida debe ser posterior a la entrada.
- El método relevante alcanza el 100 % de cobertura de instrucciones y ramas, y las pruebas detectan el defecto deliberado.
- Maven, Surefire y JaCoCo no muestran incidencias bloqueantes.
- `findByPropertyId(Long)` queda como posible necesidad de verificación futura. Si se aborda, considerar casos de disponibilidad existente y de ausencia, según el contrato.
- No perseguir un porcentaje de cobertura mayor sin un requisito explícito.

## Architecture
Java + Spring Boot + Maven, capas domain/repository/service/web.
Html usa styles.css para los html, y si consideras cambiar el styles.css dilo.

## Develoment Process


## Restrictions
- No hagas cambios directamente sin que lo revisemos
