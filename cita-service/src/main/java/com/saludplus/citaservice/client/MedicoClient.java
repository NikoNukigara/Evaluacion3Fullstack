package com.saludplus.citaservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Definimos a quién vamos a llamar (al medico-service en su respectivo puerto)
@FeignClient(name = "medico-service", url = "http://localhost:8082")
public interface MedicoClient {

    // Imitamos la ruta exacta que tendrá el médico para buscar por ID
    @GetMapping("/api/medicos/{id}")
    String obtenerMedico(@PathVariable("id") Long id);
}