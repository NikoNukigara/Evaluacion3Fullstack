package com.SaludPlus.v_1;

import com.SaludPlus.v_1.model.Paciente;
import com.SaludPlus.v_1.repository.PacienteRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Evitar poblar duplicados si ya existen datos
        if (pacienteRepository.count() > 0) {
            return;
        }

        // Generar 50 pacientes falsos
        for (int i = 0; i < 50; i++) {
            Paciente paciente = new Paciente();
            
            // Generar RUT falso simple: ej. 12345678-9
            String rutFalso = (10000000 + random.nextInt(15000000)) + "-" + faker.number().digit();
            
            paciente.setRut(rutFalso);
            paciente.setNombre(faker.name().firstName());
            paciente.setApellido(faker.name().lastName());
            paciente.setEdad(faker.number().numberBetween(18, 85));
            
            pacienteRepository.save(paciente);
        }
        System.out.println("=====================================================");
        System.out.println("  [pacientes-service] 50 pacientes creados con Faker ");
        System.out.println("=====================================================");
    }
}
