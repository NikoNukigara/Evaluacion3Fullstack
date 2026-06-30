package com.saludplus.citaservice.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
// import jakarta.annotation.PostConstruct; <-- Puedes quitar este import también

@Component
@EnableScheduling
public class BackupScheduler {

    private final BackupService backupService;

    public BackupScheduler(BackupService backupService) {
        this.backupService = backupService;
    }

    /* 🔥 TRUCO DE PRUEBA COMENTADO: Ya no se ejecutará al iniciar
    @PostConstruct
    public void probarAhora() {
        System.out.println("⏳ Iniciando prueba de backup automático para Citas...");
        backupService.createBackup();
    }
    */

    // 🌙 HORARIO REAL: 12:00 AM todos los días
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleBackup() {
        backupService.createBackup();
    }
}