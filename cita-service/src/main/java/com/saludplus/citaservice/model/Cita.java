package com.saludplus.citaservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fecha;
    private String hora;
    private String nombrePaciente;
    private String motivo;
    
    // Este campo es clave: aquí guardamos la referencia al otro microservicio
    private Long medicoId;

    // Constructores
    public Cita() {}

    public Cita(String fecha, String hora, String nombrePaciente, String motivo, Long medicoId) {
        this.fecha = fecha;
        this.hora = hora;
        this.nombrePaciente = nombrePaciente;
        this.motivo = motivo;
        this.medicoId = medicoId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    
    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    // Métodos de compatibilidad con el antiguo campo fechaHora
    public String getFechaHora() { 
        return this.fecha + " " + this.hora; 
    }
    public void setFechaHora(String fechaHora) { 
        if (fechaHora != null && fechaHora.contains(" ")) {
            String[] parts = fechaHora.split(" ");
            this.fecha = parts[0];
            this.hora = parts.length > 1 ? parts[1] : "00:00";
        }
    }
}