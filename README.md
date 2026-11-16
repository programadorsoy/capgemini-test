# Capgemini-Test

## Cambios realizado desde el original
Se ha realizado la prueba de openbank en:
 
https://github.com/capCCA/capgemini-test/

Se ha montado el proyecto en STS en un equipo local.

Cambios realizado en el software original:

pom.xml
	Se incluye la dependencia:
	<dependency>
	      <groupId>io.github.openfeign</groupId>
	      <artifactId>feign-okhttp</artifactId>
    	</dependency>  

V1_init.sql
	Se cambian los valores de INSERT INTO, añadiendo campos obligatorios y se añaden valores para esos campos.

docker-compose.yml
	Se cambia la configuración para poderlo arrancar con la version de docker Docker version 29.9.3, build 5457199.
	
Para ejecutar la BBDD, la prueba la he realizado instalando docker desktop y ejecutando en powershell docker compose up -d

Para realizar las diferentes pruebas, se puede realizar por postman o por curl:

## Pruebas

Se han realizado las pruebas con postman 12.37.0, se adjunta en el proyecto la colección creada a partir de la colección dada.
Para ponerlo en el readme, tambien se ha probado a través de curl.

## API check-dni

1.- Prueba de dni valido:
PS curl.exe -4 -i -X PATCH "http://127.0.0.1:1080/check-dni" `
>>   -H "Content-Type: application/json" `
>>   -d '{ "dni": "12345678Z" }'
HTTP/1.1 200 OK
Content-Type: application/json
connection: keep-alive
content-length: 24

{"message": "Valid DNI"}

2.- Prueba de dni invalido:
PS curl.exe -4 -i -X PATCH "http://127.0.0.1:1080/check-dni" -H "Content-Type: application/json" --data-raw '{"dni":"99999999w"}'
HTTP/1.1 409 Conflict
Content-Type: application/json
connection: keep-alive
content-length: 26

{"message": "Invalid DNI"}


## API notification
1.- Prueba de email y mensaje valido:
PS C:\Users\rambosoy\git\capgemini-test\docker> curl.exe -4 -i -X POST "http://127.0.0.1:1080/email" -H "Content-Type: application/json" --data-raw '{"email":"test@example.com","message":"hola"}'
HTTP/1.1 200 OK
Content-Type: application/json
connection: keep-alive
content-length: 26

{"message": "valid email"}

2.- Prueba de sms:
PS C:\Users\rambosoy\git\capgemini-test\docker> curl.exe -4 -i -X POST "http://127.0.0.1:1080/sms" -H "Content-Type: application/json" --data-raw '{"phone":"951234567","message":"prueba"}'
HTTP/1.1 200 OK
Content-Type: application/json
connection: keep-alive
content-length: 24

{"message": "valid sms"}

## 
#	Test generado según requisitos.
1	(Unitario)    test -Dtest=DniValidatorTest	Validación de formato DNI	Nada (puro).

2	(Unitario)    test -Dtest=RoleValidatorTest	Validación de rol	Nada (puro).

3	(Unitario)    test -Dtest=UserServiceTest	Lógica de negocio con Mockito	Nada (mocks).

4	(Unitario)    test -Dtest=DniValidationAdapterTest	Adaptador Feign con Mockito	Nada (mocks).

5	(Integración) test -Dtest=UserRepositoryAdapterTestIT	Adapter JPA + H2	H2, entidades.

6	(Integración) test -Dtest=UserControllerIT	Controlador + MockMvc	Todo el contexto.

7	(Aceptación)  test -Dtest=CreateUserAcceptanceTest	E2E + MockServer real	Docker (MockServer)

## 


## Prerrequisitos

Asegúrese de tener instalados los siguientes componentes antes de ejecutar el proyecto:

- Java 17
- Maven
- Git
- Docker

---

## Ejecución del Docker para tener la infraestructura necesaria

1. Clone el repositorio:
   bash
   git clone <url-del-repositorio>
   
2. Navegue a la carpeta docker:
   bash
   cd docker
   
3. Levante los contenedores con Docker Compose:
   bash
   docker-compose up
   

### Contenedores del Docker

- *PostgreSQL:* Base de datos.
- *Mock-server:* API expuesta en el puerto 1080.

---

## APIs Expuestas en el Mock-server

### 1. API check-dni
- *Método:* PATCH
- *URL:* http://localhost:1080/check-dni
- *Body de la petición:*
  json
  {
    "dni": "<dni>"
  }
  
- *Respuestas:*
  - *OK:* Código HTTP 200 para cualquier DNI.
  - *KO:* Código HTTP 409 si el DNI es 99999999w.

---

### 2. API notification
#### Email
- *Método:* POST
- *URL:* http://localhost:1080/email
- *Body de la petición:*
  json
  {
    "email": "<email>",
    "message": "<msg>"
  }
  
- *Respuesta:*
  - *OK:* Código HTTP 200.

#### SMS
- *Método:* POST
- *URL:* http://localhost:1080/sms
- *Body de la petición:*
  json
  {
    "phone": "<phone>",
    "message": "<msg>"
  }
  
- *Respuesta:*
  - *OK:* Código HTTP 200.

---

## Contexto

La aplicación debe gestionar salas y usuarios bajo las siguientes condiciones:

- Cada sala tiene un ID único (long incremental).
- Una sala puede contener N usuarios.
- Un usuario puede estar en una única sala.
- Al guardar el usuario en la sala, se valida su DNI contra una API externa.
- Almacena el usuario en la base de datos y notifica según su rol.
- Devuelve el ID del usuario almacenado.

---

## Requisitos

### Validaciones:
1. *Nombre:* No debe contener más de 6 caracteres.
2. *Email:* Debe contener un @ y un ..
3. *Rol:* Sólo puede ser admin o superadmin.
4. Si el usuario ya existe (por email), se debe lanzar una excepción.
5. Validar el DNI contra la API externa del mock-server.

### Notificaciones:
- *Admin:* Notificación por email con el mensaje: "usuario guardado".
- *Superadmin:* Notificación por SMS con el mensaje: "usuario guardado".

### Respuesta esperada:
- *Retornar el ID generado.*

---

## Métodos a Implementar

### 1. Crear Usuario
- *Método:* POST
- *Descripción:* Guarda un usuario en la sala 1 y retorna su ID.
- *Ejemplo de JSON a guardar:*
  json
  {
    "name": "pablo",
    "email": "email@email.com",
    "phone": "677998899",
    "rol": "admin",
    "dni": "23454234W"
  }
  
- *Respuestas:*
  - *OK:* 
    - Código HTTP 201 Created
    - Body:
      json
      {
        "id": "<id>"
      }
      
  - *KO:* 
    - Código HTTP 409 Conflict
    - Body:
      json
      {
        "code": 409,
        "message": "error validation <email | userName | dni>"
      }
      

### 2. Obtener Usuario
- *Método:* GET
- *Descripción:* Obtiene un usuario basado en su ID dentro de la sala 1.

---

## Condiciones Opcionales

1. *Escalabilidad:* La aplicación podrá escalar para manejar diferentes contextos (pagos, pedidos, etc.).
2. *Capacidad:* La aplicación podrá procesar desde una hasta millones de peticiones (no es necesario implementar Kubernetes).

---

## Cosas que se Valoran

1. *Pruebas:*
   - Unitarias.
   - De integración.
   - De aceptación.
2. *Arquitectura e implementación.*
3. *Uso de Spring y abstracción del framework.*
4. *Patrones de diseño.*

---

## Método de Entrega

- Subir el proyecto a un repositorio público personal.
- Todos los commits deben estar realizados en la rama main.
