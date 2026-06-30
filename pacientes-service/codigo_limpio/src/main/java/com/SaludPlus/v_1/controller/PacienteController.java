package com.SaludPlus.v_1.controller;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // GET: http://localhost:8080/api/v1/pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        List<Paciente> pacientes = service.obtenerTodos();
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    // GET: http://localhost:8080/api/v1/pacientes/1
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        Optional<Paciente> paciente = service.obtenerPorId(id);
        if (paciente.isPresent()) {
            return ResponseEntity.ok(paciente.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: http://localhost:8080/api/v1/pacientes
    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = service.registrarPaciente(paciente);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
    }

    // PUT: http://localhost:8080/api/v1/pacientes/1
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        Optional<Paciente> pacienteExistente = service.obtenerPorId(id);
        if (pacienteExistente.isPresent()) {
            Paciente pac = pacienteExistente.get();
            pac.setRut(paciente.getRut());
            pac.setNombre(paciente.getNombre());
            pac.setApellido(paciente.getApellido());
            pac.setEdad(paciente.getEdad());
            return ResponseEntity.ok(service.registrarPaciente(pac));
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: http://localhost:8080/api/v1/pacientes/1
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
    @PostMapping("/masivo")
    public ResponseEntity<List<Paciente>> cargaMasiva(@RequestBody List<Paciente> pacientes) {
        List<Paciente> nuevosPacientes = service.guardarMasivo(pacientes);
        return new ResponseEntity<>(nuevosPacientes, HttpStatus.CREATED);
    }
}