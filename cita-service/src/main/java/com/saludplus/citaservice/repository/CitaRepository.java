package com.saludplus.citaservice.repository;

import com.saludplus.citaservice.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByNombrePacienteAndFecha(String nombrePaciente, String fecha);
    List<Cita> findByMedicoIdAndMotivo(Long medicoId, String motivo);
    List<Cita> findByNombrePacienteAndFechaBetween(String nombrePaciente, String fechaInicio, String fechaFin);
    List<Cita> findByMedicoIdAndFechaBetween(Long medicoId, String fechaInicio, String fechaFin);
    long countByMedicoId(Long medicoId);
}
