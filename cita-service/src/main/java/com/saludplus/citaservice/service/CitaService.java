package com.saludplus.citaservice.service;

import com.saludplus.citaservice.model.Cita;
import com.saludplus.citaservice.repository.CitaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public Cita agendarCita(Cita cita) {
        return citaRepository.save(cita);
    }

    public Optional<Cita> obtenerCita(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public Cita actualizarCita(Long id, Cita citaActualizada) {
        return citaRepository.findById(id)
                .map(cita -> {
                    if (citaActualizada.getFecha() != null) {
                        cita.setFecha(citaActualizada.getFecha());
                    }
                    if (citaActualizada.getHora() != null) {
                        cita.setHora(citaActualizada.getHora());
                    }
                    if (citaActualizada.getNombrePaciente() != null) {
                        cita.setNombrePaciente(citaActualizada.getNombrePaciente());
                    }
                    if (citaActualizada.getMotivo() != null) {
                        cita.setMotivo(citaActualizada.getMotivo());
                    }
                    if (citaActualizada.getMedicoId() != null) {
                        cita.setMedicoId(citaActualizada.getMedicoId());
                    }
                    return citaRepository.save(cita);
                })
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    public void cancelarCita(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> obtenerCitasPorPacienteYFecha(String nombrePaciente, String fecha) {
        return citaRepository.findByNombrePacienteAndFecha(nombrePaciente, fecha);
    }

    public List<Cita> obtenerCitasPorMedicoYMotivo(Long medicoId, String motivo) {
        return citaRepository.findByMedicoIdAndMotivo(medicoId, motivo);
    }

    public List<Cita> obtenerCitasPorPacienteEntreFechas(String nombrePaciente, String fechaInicio, String fechaFin) {
        return citaRepository.findByNombrePacienteAndFechaBetween(nombrePaciente, fechaInicio, fechaFin);
    }

    public List<Cita> obtenerCitasPorMedicoEntreFechas(Long medicoId, String fechaInicio, String fechaFin) {
        return citaRepository.findByMedicoIdAndFechaBetween(medicoId, fechaInicio, fechaFin);
    }

    public long contarCitasPorMedico(Long medicoId) {
        return citaRepository.countByMedicoId(medicoId);
    }
}
