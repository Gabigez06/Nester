# Pago de solicitudes de alquiler — alcance inicial

## Objetivo

Definir cómo se simularán los pagos de las solicitudes de alquiler aprobadas por el propietario. Esta fase no realiza cobros reales ni guarda datos financieros.

## Alcance

- El propietario debe aprobar la solicitud antes de que el inquilino pueda pagar.
- La aprobación no inicia un cobro automático: se notifica al inquilino para que elija un método y complete el pago.
- Se contemplan tarjeta de crédito, tarjeta de débito y PayPal como métodos seleccionables.
- El pago es simulado: la aplicación registra el método y el resultado, pero no solicita ni almacena números de tarjeta, códigos de seguridad ni credenciales de PayPal.
- Desde que se aprueba la solicitud y hasta que el pago se completa, las fechas permanecen bloqueadas para otras solicitudes.

## Importe

El servidor calcula el total a partir del precio por noche del inmueble y las fechas de la reserva. No se debe confiar en un importe enviado por el navegador.

## Estados del pago

- `PENDIENTE`: el intento de pago se ha iniciado.
- `COMPLETADO`: el pago simulado ha terminado correctamente.
- `FALLIDO`: el pago simulado no se ha completado.

## Flujo básico

1. El inquilino envía una solicitud de alquiler y el propietario la revisa.
2. Si el propietario la rechaza, no se realiza ningún pago.
3. Si la aprueba, se notifica al inquilino y las fechas quedan bloqueadas mientras se completa el pago.
4. El inquilino selecciona un método de pago. El servidor calcula el importe y registra el intento como `PENDIENTE`.
5. La simulación devuelve un resultado. Si tiene éxito, el pago pasa a `COMPLETADO` y la reserva queda confirmada.
6. Si el intento de pago falla, pasa a `FALLIDO`; el inquilino puede iniciar otro intento. Las fechas siguen bloqueadas hasta que el pago se complete.

## Fuera de alcance por ahora

- Integración con una pasarela real.
- Reembolsos y cancelaciones.
- Reservas inmediatas que no requieren aprobación del propietario.
- Almacenamiento de datos de tarjetas o cuentas de PayPal.
