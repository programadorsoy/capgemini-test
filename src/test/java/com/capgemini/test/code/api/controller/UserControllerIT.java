package com.capgemini.test.code.api.controller;

/*
 * Test de integracion
 * 
 * Levanta el contexto de Spring y prueba el endpoint POST /users con MockMvc. 
 * Verifica que una petición válida devuelve 201 Created con {"id":...}, 
 * y que los casos de error (email duplicado, nombre >6 caracteres, rol inválido, DNI inválido) 
 * devuelven 409 Conflict con {"code":409,"message":"..."}. 
 * Los puertos (DniValidationPort, NotificationPort) se mockean con @MockBean. 
 * 
 */
public class UserControllerIT {

}
