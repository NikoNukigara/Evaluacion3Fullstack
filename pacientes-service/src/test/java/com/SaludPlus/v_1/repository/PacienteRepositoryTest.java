package com.SaludPlus.v_1.repository;

import com.SaludPlus.v_1.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@org.springframework.test.context.ActiveProfiles("test")
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @DisplayName("Debe guardar un paciente exitosamente en la base de datos H2")
    void shouldSavePacienteSuccessfully() {
        // Arrange
        Paciente paciente = new Paciente(null, "11111111-1", "Carlos", "Lopez", 40);

        // Act
        Paciente savedPaciente = pacienteRepository.save(paciente);

        // Assert
        assertNotNull(savedPaciente);
        assertNotNull(savedPaciente.getId());
        assertEquals("Carlos", savedPaciente.getNombre());
    }

    @Test
    @DisplayName("Debe encontrar un paciente por ID")
    void shouldFindPacienteById() {
        // Arrange
        Paciente paciente = new Paciente(null, "22222222-2", "Maria", "Silva", 28);
        Paciente savedPaciente = pacienteRepository.save(paciente);

        // Act
        Optional<Paciente> foundPaciente = pacienteRepository.findById(savedPaciente.getId());

        // Assert
        assertTrue(foundPaciente.isPresent());
        assertEquals("22222222-2", foundPaciente.get().getRut());
    }
}
