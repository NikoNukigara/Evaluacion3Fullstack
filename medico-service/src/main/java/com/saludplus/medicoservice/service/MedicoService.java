package com.saludplus.medicoservice.service;

import com.saludplus.medicoservice.model.Medico;
import com.saludplus.medicoservice.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public Medico crearMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public Optional<Medico> obtenerMedico(Long id) {
        return medicoRepository.findById(id);
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Medico actualizarMedico(Long id, Medico medicoActualizado) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    if (medicoActualizado.getNombre() != null) {
                        medico.setNombre(medicoActualizado.getNombre());
                    }
                    if (medicoActualizado.getEspecialidad() != null) {
                        medico.setEspecialidad(medicoActualizado.getEspecialidad());
                    }
                    if (medicoActualizado.getTelefono() != null) {
                        medico.setTelefono(medicoActualizado.getTelefono());
                    }
                    if (medicoActualizado.getCorreo() != null) {
                        medico.setCorreo(medicoActualizado.getCorreo());
                    }
                    return medicoRepository.save(medico);
                })
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + id));
    }

    public void eliminarMedico(Long id) {
        medicoRepository.deleteById(id);
    }
}
