package com.saludplus.citaservice.controller;

import com.saludplus.citaservice.client.MedicoClient;
import com.saludplus.citaservice.model.Cita;
import com.saludplus.citaservice.service.CitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Operaciones relacionadas con las citas médicas")
public class CitaController {

    private final MedicoClient medicoClient;
    private final CitaService citaService;

    public CitaController(MedicoClient medicoClient, CitaService citaService) {
        this.medicoClient = medicoClient;
        this.citaService = citaService;
    }

    // POST - Agendar una nueva cita
    @Operation(summary = "Agendar una cita", description = "Agenda una nueva cita para un médico específico")
    @PostMapping("/agendar/{medicoId}")
    public ResponseEntity<?> agendarCita(@PathVariable Long medicoId, @RequestBody Cita cita) {
        // Validar que el médico existe (llamada a medico-service)
        try {
            medicoClient.obtenerMedico(medicoId);
            cita.setMedicoId(medicoId);
            Cita citaAgendada = citaService.agendarCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(citaAgendada);
        } catch (Exception e) {
            String mensaje = "No se pudo agendar la cita. Verifique que el medico-service esté corriendo en el puerto 8082 y que el médico con ID "
                    + medicoId + " exista. Error: " + e.getMessage();
            return ResponseEntity.badRequest().body(java.util.Map.of("error", mensaje));
        }
    }

    // GET - Obtener una cita por ID
    @Operation(summary = "Obtener cita por ID", description = "Obtiene los detalles de una cita específica")
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerCita(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Listar todas las citas
    @Operation(summary = "Listar todas las citas", description = "Obtiene una lista de todas las citas agendadas")
    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    // PUT - Actualizar una cita existente
    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            Cita citaActualizada = citaService.actualizarCita(id, cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Cancelar una cita
    @Operation(summary = "Cancelar cita", description = "Cancela y elimina una cita existente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        if (citaService.obtenerCita(id).isPresent()) {
            citaService.cancelarCita(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}