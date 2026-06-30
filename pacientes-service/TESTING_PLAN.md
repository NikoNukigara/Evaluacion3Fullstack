## Pruebas Unitarias y Cobertura de Reglas de Negocio

### Reglas de Negocio Críticas del Servicio de Pacientes

1. **Validación de Integridad de Entidad:** Un paciente no debe poder inicializarse con atributos inválidos o inconsistentes con la lógica del negocio de SaludPlus.
2. **Aislamiento de Persistencia:** Los tests del repositorio no deben impactar la base de datos MySQL real de producción/desarrollo (Uso de H2 en memoria).
3. **Manejo de Errores de Existencia:** Si se busca o elimina un paciente que no existe, el sistema o controlador debería ser capaz de gestionar o delegar correctamente dicho estado sin fallar inesperadamente.

### Cobertura Actual de Pruebas Unitarias

| Regla | Estado | Casos Cubiertos | Checklist |
| :--- | :--- | :--- | :--- |
| 1. Validación de Integridad | **Cubierta** | Setter/Getter consistencia, Entidad básica. | Caso feliz. Caso de error. |
| 2. Aislamiento Persistencia | **Cubierta** | Pruebas de guardado/búsqueda en H2 (sin tocar MySQL). | H2 configurado en `@DataJpaTest`. |
| 3. Manejo Errores Existencia| **Pendiente** | (Implementación programada para Controller/Service test). | Evaluar retorno de `Optional.empty()`. |

### Reflexión y Deuda Técnica

* **Riesgo sin probar:** Al registrar un paciente, el sistema de validación (anotaciones `@Valid`) sobre formato de RUT, edad u otros no está probándose exhaustivamente si dichas validaciones no existen aún en la entidad.
* **Acción Futura:** Agregar validadores estrictos en la clase `Paciente` (usando `spring-boot-starter-validation`) y cubrirlos con pruebas unitarias que intercepten `MethodArgumentNotValidException`.
