package com.capgemini.test.code.acceptance;

/*
 * Test de aceptacion
 * 
 *  Prueba el flujo completo end-to-end: arranca la aplicación en un puerto aleatorio, 
 *  hace una petición HTTP real a POST /users con TestRestTemplate, y verifica que la respuesta es 201 con id. 
 *  A diferencia del IT, aquí no se mockean los puertos: se espera que el MockServer real esté corriendo en 127.0.0.1:1080 
 *  para validar el DNI y enviar la notificación. Es el test que simula el comportamiento real del sistema.
 * 
 */
public class CreateUserAcceptanceTest {

}
