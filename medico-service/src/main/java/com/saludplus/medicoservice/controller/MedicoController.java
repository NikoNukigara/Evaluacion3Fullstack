package com.saludplus.medicoservice.controller;

import com.saludplus.medicoservice.model.Medico;
import com.saludplus.medicoservice.service.MedicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Operaciones relacionadas con los médicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // GET - Obtener un médico por ID
    @Operation(summary = "Obtener médico por ID", description = "Obtiene los detalles de un médico específico")
    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtenerMedico(@PathVariable Long id) {
        return medicoService.obtenerMedico(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Listar todos los médicos
    @Operation(summary = "Listar todos los médicos", description = "Obtiene una lista de todos los médicos registrados")
    @GetMapping
    public ResponseEntity<List<Medico>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    // POST - Crear un nuevo médico
    @Operation(summary = "Crear médico", description = "Crea y registra un nuevo médico")
    @PostMapping
    public ResponseEntity<Medico> crearMedico(@RequestBody Medico medico) {
        Medico medicoCreado = medicoService.crearMedico(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoCreado);
    }

    // PUT - Actualizar un médico existente
    @Operation(summary = "Actualizar médico", description = "Actualiza los datos de un médico existente")
    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizarMedico(@PathVariable Long id, @RequestBody Medico medico) {
        try {
            Medico medicoActualizado = medicoService.actualizarMedico(id, medico);
            return ResponseEntity.ok(medicoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar un médico
    @Operation(summary = "Eliminar médico", description = "Elimina un médico existente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        if (medicoService.obtenerMedico(id).isPresent()) {
            medicoService.eliminarMedico(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}