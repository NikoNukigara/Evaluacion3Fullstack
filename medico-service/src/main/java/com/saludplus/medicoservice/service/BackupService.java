package com.saludplus.medicoservice.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class BackupService {

    public void createBackup() {
        // 1. RUTA AL MOTOR DE LARAGON
        String dumpPath = "C:/laragon/bin/mysql/mysql-8.4.3-winx64/bin/mysqldump.exe";
        String dbName = "medico_db";
        String dbUser = "root";

        // 2. RUTA DE TU PROYECTO
        String savePath = "C:/Users/ALIENWARE X15 R1/OneDrive/Escritorio/fullstack 1/backups/backup_medico_service.sql";

        // 3. COMANDOS BLINDADOS
        String[] command = new String[]{
                dumpPath,
                "--no-defaults",       // 🔥 EL ESCUDO: Ignora el archivo de Laragon que tiene el error 'quick'
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