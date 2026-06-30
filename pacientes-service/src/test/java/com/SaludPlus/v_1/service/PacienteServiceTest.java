package com.SaludPlus.v_1.service;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("Debe retornar todos los pacientes exitosamente")
    void shouldReturnAllPacientesSuccessfully() {
        // Arrange
        Paciente paciente = new Paciente(1L, "12345678-9", "Juan", "Perez", 30);
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        // Act
        List<Paciente> result = pacienteService.obtenerTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe guardar un paciente exitosamente")
    void shouldSavePacienteSuccessfully() {
        // Arrange
        Paciente paciente = new Paciente(null, "12345678-9", "Juan", "Perez", 30);
        Paciente savedPaciente = new Paciente(1L, "12345678-9", "Juan", "Perez", 30);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(savedPaciente);

        // Act
        Paciente result = pacienteService.registrarPaciente(paciente);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12345678-9", result.getRut());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("Debe eliminar un paciente por ID exitosamente")
    void shouldDeletePacienteById() {
        // Arrange
        Long pacienteId = 1L;
        doNothing().when(pacienteRepository).deleteById(pacienteId);

        // Act
        pacienteService.eliminarPaciente(pacienteId);

        // Assert
        verify(pacienteRepository, times(1)).deleteById(pacienteId);
    }
}
