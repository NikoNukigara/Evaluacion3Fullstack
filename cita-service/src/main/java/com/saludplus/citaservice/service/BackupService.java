package com.saludplus.citaservice.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class BackupService {

    public void createBackup() {
        // 1. RUTA AL MOTOR DE LARAGON (La que descubrimos ayer)
        String dumpPath = "C:/laragon/bin/mysql/mysql-8.4.3-winx64/bin/mysqldump.exe";
        String dbName = "cita_db"; // <-- Apuntando a la BD de citas
        String dbUser = "root";

        // 2. RUTA DE TU PROYECTO
        String savePath = "C:/Users/ALIENWARE X15 R1/OneDrive/Escritorio/fullstack 1/backups/backup_cita_service.sql";

        // 3. COMANDOS BLINDADOS (Con el escudo antimanchas de Laragon)
        String[] command = new String[]{
                dumpPath,
                "--no-defaults",
                "-h", "127.0.0.1",
                "-P", "3307",
                "-u", dbUser,
                "--databases", dbName,
                "-r", savePath
        };

        try {
            Process process = Runtime.getRuntime().exec(command);

            // Detector de errores
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("MENSAJE DE MYSQL: " + errorLine);
            }

            int processComplete = process.waitFor();

            if (processComplete == 0) {
                System.out.println("✅ Backup creado con éxito en: " + savePath);
            } else {
                System.err.println("❌ Fallo al crear el backup. Código de salida: " + processComplete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}