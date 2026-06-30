package com.saludplus.citaservice.controller;

import com.saludplus.citaservice.assembler.CitaModelAssembler;
import com.saludplus.citaservice.model.Cita;
import com.saludplus.citaservice.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/citas")
@Tag(name = "Citas V2 HATEOAS", description = "Operaciones de HATEOAS y Reportes")
public class CitaControllerV2 {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todas las citas (V2)")
    public CollectionModel<EntityModel<Cita>> listarTodas() {
        List<EntityModel<Cita>> citas = citaService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(citas,
                linkTo(methodOn(CitaControllerV2.class).listarTodas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener cita por ID (V2)")
    public ResponseEntity<EntityModel<Cita>> obtenerPorId(@PathVariable Long id) {
        return citaService.obtenerCita(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 1. Obtener todas las citas de un paciente en una fecha específica (Estudiante / Fecha)
    @GetMapping(value = "/paciente/{nombrePaciente}/fecha/{fecha}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Reporte 1: Citas por paciente y fecha")
    public CollectionModel<EntityModel<Cita>> obtenerPorPacienteYFecha(
            @PathVariable String nombrePaciente, @PathVariable String fecha) {
        
        List<EntityModel<Cita>> citas = citaService.obtenerCitasPorPacienteYFecha(nombrePaciente, fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return CollectionModel.of(citas,
                linkTo(methodOn(CitaControllerV2.class).obtenerPorPacienteYFecha(nombrePaciente, fecha)).withSelfRel());
    }

    // 2. Obtener todas las citas de un médico en un motivo específico (Sala / Estado)
    @GetMapping(value = "/medico/{medicoId}/motivo/{motivo}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Reporte 2: Citas por médico y motivo")
    public CollectionModel<EntityModel<Cita>> obtenerPorMedicoYMotivo(
            @PathVariable Long medicoId, @PathVariable String motivo) {
        
        List<EntityModel<Cita>> citas = citaService.obtenerCitasPorMedicoYMotivo(medicoId, motivo).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return CollectionModel.of(citas,
                linkTo(methodOn(CitaControllerV2.class).obtenerPorMedicoYMotivo(medicoId, motivo)).withSelfRel());
    }

    // 3. Obtener todas las citas de un paciente entre dos fechas (Estudiante / Entre fechas)
    @GetMapping(value = "/paciente/{nombrePaciente}/fechas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Reporte 3: Citas por paciente entre dos fechas")
    public CollectionModel<EntityModel<Cita>> obtenerPorPacienteEntreFechas(
            @PathVariable String nombrePaciente, 
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        
        List<EntityModel<Cita>> citas = citaService.obtenerCitasPorPacienteEntreFechas(nombrePaciente, fechaInicio, fechaFin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return CollectionModel.of(citas,
                linkTo(methodOn(CitaControllerV2.class).obtenerPorPacienteEntreFechas(nombrePaciente, fechaInicio, fechaFin)).withSelfRel());
    }

    // 4. Obtener todas las citas de un médico entre dos fechas (Sala / Entre fechas)
    @GetMapping(value = "/medico/{medicoId}/fechas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Reporte 4: Citas por médico entre dos fechas")
    public CollectionModel<EntityModel<Cita>> obtenerPorMedicoEntreFechas(
            @PathVariable Long medicoId, 
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        
        List<EntityModel<Cita>> citas = citaService.obtenerCitasPorMedicoEntreFechas(medicoId, fechaInicio, fechaFin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return CollectionModel.of(citas,
                linkTo(methodOn(CitaControllerV2.class).obtenerPorMedicoEntreFechas(medicoId, fechaInicio, fechaFin)).withSelfRel());
    }

    // 5. Obtener el total de citas realizadas con un médico específico (Total reservas / Sala)
    @GetMapping(value = "/medico/{medicoId}/total", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Reporte 5: Total de citas por médico")
    public ResponseEntity<Long> obtenerTotalPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(citaService.contarCitasPorMedico(medicoId));
    }
}
