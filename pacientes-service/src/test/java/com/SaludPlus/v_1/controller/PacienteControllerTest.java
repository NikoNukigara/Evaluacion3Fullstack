package com.SaludPlus.v_1.controller;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.SaludPlus.v_1.security.JwtService;
import com.SaludPlus.v_1.security.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PacienteController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.ActiveProfiles("test")
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;
    
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe retornar 200 OK y una lista de pacientes al hacer GET a /api/v1/pacientes")
    void shouldReturnOkWhenGetAllPacientes() throws Exception {
        // Arrange
        Paciente paciente = new Paciente(1L, "12345678-9", "Juan", "Perez", 30);
        when(pacienteService.obtenerTodos()).thenReturn(List.of(paciente));

        // Act & Assert
        mockMvc.perform(get("/api/v1/pacientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pacienteList[0].nombre").value("Juan"))
                .andExpect(jsonPath("$._embedded.pacienteList[0].rut").value("12345678-9"));
    }

    @Test
    @DisplayName("Debe retornar 201 Created al guardar un paciente con POST a /api/v1/pacientes")
    void shouldReturnCreatedWhenSavePaciente() throws Exception {
        // Arrange
        Paciente paciente = new Paciente(null, "12345678-9", "Juan", "Perez", 30);
        Paciente savedPaciente = new Paciente(1L, "12345678-9", "Juan", "Perez", 30);
        
        when(pacienteService.registrarPaciente(any(Paciente.class))).thenReturn(savedPaciente);

        // Act & Assert
        mockMvc.perform(post("/api/v1/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
}
