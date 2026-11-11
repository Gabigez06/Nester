# Pago de reservas — alcance inicial

## Objetivo

Definir cómo se simularán los pagos de las reservas inmediatas. Esta fase no realiza cobros reales ni guarda datos financieros.

## Alcance

- El pago se realiza para una reserva inmediata.
- Se contemplan tarjeta de crédito, tarjeta de débito y PayPal como métodos seleccionables.
- El pago es simulado: la aplicación registra el método y el resultado, pero no solicita ni almacena números de tarjeta, códigos de seguridad ni credenciales de PayPal.
- Las solicitudes de reserva que esperan la aprobación del propietario quedan fuera de esta fase.

## Importe

El servidor calcula el total a partir del precio por noche del inmueble y las fechas de la reserva. No se debe confiar en un importe enviado por el navegador.

## Estados del pago

- `PENDIENTE`: el intento de pago se ha iniciado.
- `COMPLETADO`: el pago simulado ha terminado correctamente.
- `FALLIDO`: el pago simulado no se ha completado.

## Flujo básico

1. El inquilino inicia una reserva inmediata y selecciona un método de pago.
2. El servidor calcula el importe y registra el intento como `PENDIENTE`.
3. La simulación devuelve un resultado.
4. Si tiene éxito, el pago pasa a `COMPLETADO`; si falla, pasa a `FALLIDO`.
5. La reserva solo se confirma cuando el pago se completa.

## Fuera de alcance por ahora

- Integración con una pasarela real.
- Reembolsos y cancelaciones.
- Cobros asociados a solicitudes que requieren aprobación del propietario.
- Almacenamiento de datos de tarjetas o cuentas de PayPal.
