package com.SaludPlus.v_1.service;

import com.SaludPlus.v_1.dto.ClienteDTO;
import com.SaludPlus.v_1.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CargaMasivaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void procesarCarga(List<ClienteDTO> listaDto) {
        int batchSize = 50;
        for (int i = 0; i < listaDto.size(); i++) {
            ClienteDTO dto = listaDto.get(i);

            Cliente cliente = new Cliente();
            cliente.setRut(dto.getRut());
            cliente.setNombreCompleto(dto.getNombreCompleto());
            cliente.setEmail(dto.getEmail());
            cliente.setTelefono(dto.getTelefono());

            entityManager.persist(cliente);

            // Cada 50 registros, enviamos a la BD y limpiamos RAM
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}