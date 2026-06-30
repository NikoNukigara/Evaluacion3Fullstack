# Especificación de Requisitos de Software (ERS)

**Proyecto:** Clínica SaludPlus  
**Consultora:** MedTech Solutions SpA  
**Fecha de entrega:** Junio 30, 2026  
**Integrantes:** Barbara Oyarzun, Roberto Gonzalez, Daniel Navarro  

---

## 1. Documentación del Sistema y Arquitectura

SaludPlus funciona como un ecosistema distribuido mediante microservicios, eliminando completamente el cuello de botella de los monolitos. La plataforma ha evolucionado para incluir enrutamiento dinámico y descubrimiento de servicios, desglosándose en los siguientes componentes independientes:

* **Servidor de Descubrimiento (Puerto 8761):** Servidor Eureka de Spring Cloud (Netflix OSS). Actúa como registro central donde todos los servicios reportan su ubicación.
* **API Gateway (Puerto 8080):** Enrutador central construido con Spring Cloud Gateway. Centraliza el tráfico y redirige peticiones mediante balanceo de carga (`lb://`) aislando la red interna de los clientes.
* **Microservicio Pacientes (Puerto 8081):** Núcleo de seguridad (Spring Security + JWT), gestión de pacientes (CRUD) y orquestación de accesos.
* **Microservicio Médicos (Puerto 8082):** Catálogo específico de facultativos y especialidades médicas.
* **Microservicio Citas (Puerto 8083):** Gestión de la agenda y validación de la relación médico-paciente.

**Arquitectura y Flujo de Datos:** 
La comunicación externa se dirige únicamente al **API Gateway**, el cual distribuye la carga de manera síncrona hacia los servicios (REST API). El servicio de Pacientes intercepta y valida las credenciales (JWT) cuando se requiere seguridad. La comunicación interna entre servicios se maneja mediante `RestTemplate` o `OpenFeign`. Cada servicio posee aislamiento total de datos con su propia base de datos MySQL independiente.

**Documentación con OpenAPI (Swagger):** 
Se ha integrado `springdoc-openapi` para documentar la API en cada microservicio.
* **Anotaciones clave utilizadas:**
  * `@Tag`: Para agrupar los endpoints (ej. "Pacientes V2").
  * `@Operation`: Para describir la funcionalidad de cada método.
  * `@ApiResponse`: Para documentar los códigos de estado HTTP esperados (200, 201, 401, 404).
  * `@SecurityRequirement`: Para indicar que la ruta requiere autenticación (`bearerAuth`).

---

## 2. Plan de Pruebas (Básico)

Se implementó una estrategia de pruebas sólida en dos capas para garantizar la resiliencia clínica:
1. **Pruebas Unitarias (JUnit + Mockito):** Validación de lógica de controladores y servicios aislando las dependencias mediante mocks.
2. **Pruebas de Integración:** Validación del flujo completo de comunicación entre los servicios para asegurar que el `RestTemplate` consuma correctamente los endpoints del microservicio de citas.

| Nombre del test | Clase testeada | Objetivo del test | Resultado |
| :--- | :--- | :--- | :--- |
| `listar_sinDatos_return` | `PacienteController` | Verificar estado 200 con lista vacía | **PASSED** |
| `guardar_retorna201` | `PacienteController` | Validar creación exitosa | **PASSED** |
| `listar_citas_medico` | `CitaController` | Validar consulta por ID de médico | **PASSED** |
| `acceso_no_autorizado` | `SecurityConfig` | Validar bloqueo sin JWT | **PASSED** |

---

## 3. Documentación con HATEOAS

HATEOAS (*Hypermedia as the Engine of Application State*) permite que nuestra API sea autodescriptiva. Se utilizó para que cada respuesta JSON incluya metadatos (`_links`), indicando al cliente qué acciones puede realizar a continuación (navegabilidad).

**Implementación:**
* **Framework:** Spring HATEOAS.
* **Ensambladores:** Se implementaron clases `ModelAssembler` (ej. `PacienteModelAssembler`) que convierten objetos de dominio (`Paciente`) a `EntityModel`, inyectando enlaces dinámicos hacia los métodos del controlador.
* **Respuesta JSON:** Cada entidad incluye una sección `_links` con los atributos `self` (detalle del recurso) y `reservas` (relaciones de navegación).
* **Navegabilidad:** El cliente no necesita construir URLs manualmente; sigue los enlaces proporcionados por la API, mejorando el acoplamiento y facilitando la evolución del sistema.

---

## 4. Población de Datos Automatizada (Datafaker)

Para dotar al sistema de datos realistas durante las fases de testeo y demostración, se integró un sistema distribuido de inyección de datos (Dummy Data).

* **Librería:** `net.datafaker:datafaker:2.3.0`
* **Mecanismo:** Componentes `DataLoader` que implementan `CommandLineRunner` bajo el perfil `@Profile("dev")`.
* **Volumen de inyección:** 50 Pacientes, 10 Médicos y 30 Citas aleatorias creadas de forma autónoma al inicializar los contenedores, previniendo duplicados validando la inexistencia previa de datos (`count() > 0`).

---

## 5. Despliegue en la Nube (Hipotético)

Se selecciona Render por su soporte nativo para aplicaciones Java y despliegue directo desde GitHub.

**Pasos Teóricos de Despliegue:**
1. **Configuración de Entorno:** Ajuste de variables de entorno para puerto (Render inyecta `PORT`) y credenciales de base de datos (evitando *hardcoding* de contraseñas).
2. **Deploy desde GitHub:** Conexión del repositorio. Render detecta el `pom.xml`, ejecuta `./mvnw clean package` y levanta el `.jar`.
3. **Supervisión:** Uso de los Logs nativos de Render para monitorear el arranque de Spring Boot.
4. **Monitoreo:** Implementación de `Spring Boot Actuator` en el endpoint `/actuator/health` para que el servicio de monitoreo verifique la disponibilidad del servicio automáticamente.

---

## 6. Conclusiones y Reflexión

* **Lecciones:** La mayor enseñanza fue comprender la importancia del aislamiento. Un cambio en la base de datos del servicio de citas no afecta al de médicos, lo cual es vital para la estabilidad clínica.
* **Calidad:** La calidad mejoró drásticamente al separar responsabilidades. Las anotaciones de Swagger y la implementación de HATEOAS hicieron que nuestra API sea profesional y fácil de consumir para terceros.
* **Escalabilidad y Evolución Arquitectónica:** Logramos materializar la visión inicial del proyecto incorporando un **API Gateway** centralizado, acompañado de un registro **Eureka Server**. Esto nos ha permitido unificar el punto de entrada y balancear cargas eficientemente. El uso de Docker a futuro permitirá desplegar todos estos microservicios en cualquier entorno sin configurar los puertos o el servidor manualmente.

---

## 7. Anexos
* **Repositorio GitHub:** [Enlace pendiente del repositorio]
* **Local host Gateway:** `http://localhost:8080/`
* **Local host Swagger Gateway:** `http://localhost:8080/webjars/swagger-ui/index.html` (Ruta configurada para consultar las interfaces en base a las reglas del gateway).
