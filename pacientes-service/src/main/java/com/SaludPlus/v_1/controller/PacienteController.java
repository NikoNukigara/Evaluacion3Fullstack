package com.SaludPlus.v_1.controller;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Operaciones relacionadas con los pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // GET: http://localhost:8080/api/v1/pacientes
    @Operation(summary = "Listar todos los pacientes", description = "Obtiene una lista de todos los pacientes registrados")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Paciente>>> listarTodos() {
        List<EntityModel<Paciente>> pacientes = service.obtenerTodos().stream()
                .map(paciente -> EntityModel.of(paciente,
                        linkTo(methodOn(PacienteController.class).obtenerPorId(paciente.getId())).withSelfRel(),
                        linkTo(methodOn(PacienteController.class).listarTodos()).withRel("pacientes")))
                .collect(Collectors.toList());

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(pacientes,
                linkTo(methodOn(PacienteController.class).listarTodos()).withSelfRel()));
    }

    // GET: http://localhost:8080/api/v1/pacientes/1
    @Operation(summary = "Obtener paciente por ID", description = "Obtiene los detalles de un paciente específico")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> obtenerPorId(@PathVariable Long id) {
        Optional<Paciente> paciente = service.obtenerPorId(id);
        if (paciente.isPresent()) {
            EntityModel<Paciente> recurso = EntityModel.of(paciente.get(),
                    linkTo(methodOn(PacienteController.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(PacienteController.class).listarTodos()).withRel("pacientes"));
            return ResponseEntity.ok(recurso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: http://localhost:8080/api/v1/pacientes
    @Operation(summary = "Crear paciente", description = "Crea y registra un nuevo paciente")
    @PostMapping
    public ResponseEntity<EntityModel<Paciente>> crearPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = service.registrarPaciente(paciente);
        EntityModel<Paciente> recurso = EntityModel.of(nuevoPaciente,
                linkTo(methodOn(PacienteController.class).obtenerPorId(nuevoPaciente.getId())).withSelfRel(),
                linkTo(methodOn(PacienteController.class).listarTodos()).withRel("pacientes"));
        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    // PUT: http://localhost:8080/api/v1/pacientes/1
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        Optional<Paciente> pacienteExistente = service.obtenerPorId(id);
        if (pacienteExistente.isPresent()) {
            Paciente pac = pacienteExistente.get();
            pac.setRut(paciente.getRut());
            pac.setNombre(paciente.getNombre());
            pac.setApellido(paciente.getApellido());
            pac.setEdad(paciente.getEdad());
            
            Paciente actualizado = service.registrarPaciente(pac);
            EntityModel<Paciente> recurso = EntityModel.of(actualizado,
                    linkTo(methodOn(PacienteController.class).obtenerPorId(actualizado.getId())).withSelfRel(),
                    linkTo(methodOn(PacienteController.class).listarTodos()).withRel("pacientes"));
            
            return ResponseEntity.ok(recurso);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: http://localhost:8080/api/v1/pacientes/1
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente existente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        try {
            service.eliminarPaciente(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    // POST: http://localhost:8080/api/v1/pacientes/masivo
    @Operation(summary = "Carga masiva", description = "Registra una lista de pacientes de forma masiva")
    @PostMapping("/masivo")
    public ResponseEntity<CollectionModel<EntityModel<Paciente>>> cargaMasiva(@RequestBody List<Paciente> pacientes) {
        List<EntityModel<Paciente>> nuevosPacientes = service.guardarMasivo(pacientes).stream()
                .map(paciente -> EntityModel.of(paciente,
                        linkTo(methodOn(PacienteController.class).obtenerPorId(paciente.getId())).withSelfRel(),
                        linkTo(methodOn(PacienteController.class).listarTodos()).withRel("pacientes")))
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(CollectionModel.of(nuevosPacientes,
                linkTo(methodOn(PacienteController.class).listarTodos()).withSelfRel()), HttpStatus.CREATED);
    }
}