package com.SaludPlus.v_1.service;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Garantiza que si hay un error al guardar, no se corrompa la base de datos
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    // Obtener todos los pacientes de MySQL
    public List<Paciente> obtenerTodos() {
        return repository.findAll();
    }

    // Obtener un paciente por ID
    public Optional<Paciente> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    // Guardar (Crear o Actualizar) un paciente
    public Paciente registrarPaciente(Paciente paciente) {
        return repository.save(paciente);
    }

    // Eliminar un paciente
    public void eliminarPaciente(Long id) {
        repository.deleteById(id);
    }
    // Guardar lista masiva de pacientes
    public List<Paciente> guardarMasivo(List<Paciente> pacientes) {
        return repository.saveAll(pacientes);
    }
}