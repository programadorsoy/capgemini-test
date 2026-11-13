package com.capgemini.test.code.domain.service;

/*
 *   Tests unitarios  
 * Prueba la lógica de UserService aislando sus dependencias (UserRepository, DniValidationPort, NotificationPort). 
 * Verifica: que un email duplicado lanza DuplicateEmailException; 
 * que un DNI inválido lanza InvalidDniException; 
 * que un admin notifica por email y un superadmin por SMS; 
 * y que createUser devuelve el ID generado. No carga contexto de Spring.
 */

public class UserServiceTest {

}
