package com.SaludPlus.v_1.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    @Test
    @DisplayName("Debe crear un paciente correctamente con el constructor lleno")
    void shouldCreatePacienteWithAllArgsConstructor() {
        // Arrange & Act
        Paciente paciente = new Paciente(1L, "12345678-9", "Juan", "Perez", 30);

        // Assert
        assertNotNull(paciente);
        assertEquals(1L, paciente.getId());
        assertEquals("12345678-9", paciente.getRut());
        assertEquals("Juan", paciente.getNombre());
        assertEquals("Perez", paciente.getApellido());
        assertEquals(30, paciente.getEdad());
    }

    @Test
    @DisplayName("Debe permitir establecer y obtener atributos usando Setters y Getters")
    void shouldSetAndGetAttributesSuccessfully() {
        // Arrange
        Paciente paciente = new Paciente();

        // Act
        paciente.setId(2L);
        paciente.setRut("98765432-1");
        paciente.setNombre("Ana");
        paciente.setApellido("Gomez");
        paciente.setEdad(25);

        // Assert
        assertAll("Verificación de atributos",
                () -> assertEquals(2L, paciente.getId()),
                () -> assertEquals("98765432-1", paciente.getRut()),
                () -> assertEquals("Ana", paciente.getNombre()),
                () -> assertEquals("Gomez", paciente.getApellido()),
                () -> assertEquals(25, paciente.getEdad())
        );
    }
}
