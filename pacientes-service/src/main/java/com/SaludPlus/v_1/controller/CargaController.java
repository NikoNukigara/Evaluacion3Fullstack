package com.SaludPlus.v_1.controller;

import com.SaludPlus.v_1.dto.ClienteDTO;
import com.SaludPlus.v_1.service.CargaMasivaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class CargaController {

    @Autowired
    private CargaMasivaService service;

    @PostMapping("/masivo")
    public ResponseEntity<?> cargar(@RequestBody List<ClienteDTO> clientes) {
        try {
            if (clientes == null || clientes.isEmpty()) {
                return ResponseEntity.badRequest().body("La lista está vacía");
            }
            long inicio = System.currentTimeMillis();

            service.procesarCarga(clientes);

            long fin = System.currentTimeMillis();
            return ResponseEntity.ok("Éxito: " + clientes.size() + " clientes procesados en " + (fin - inicio) + "ms");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la carga: " + e.getMessage());
        }
    }
}