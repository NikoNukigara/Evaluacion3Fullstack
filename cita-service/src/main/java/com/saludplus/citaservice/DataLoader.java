package com.saludplus.citaservice;

import com.saludplus.citaservice.model.Cita;
import com.saludplus.citaservice.repository.CitaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        if (citaRepository.count() > 0) {
            return;
        }

        List<String> motivos = Arrays.asList("Control de rutina", "Dolor agudo", "Lectura de exámenes", "Renovación de recetas", "Consulta general");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < 30; i++) {
            Cita cita = new Cita();
            
            // Fecha aleatoria en el futuro (hasta 30 días)
            java.util.Date futureDate = faker.date().future(30, TimeUnit.DAYS);
            cita.setFecha(dateFormat.format(futureDate));
            cita.setHora(timeFormat.format(futureDate));
            
            cita.setNombrePaciente(faker.name().fullName());
            cita.setMotivo(motivos.get(random.nextInt(motivos.size())));
            
            // Referencia al ID del médico generado en medico-service (1 al 10)
            cita.setMedicoId((long) (1 + random.nextInt(10)));
            
            citaRepository.save(cita);
        }
        System.out.println("=====================================================");
        System.out.println("     [cita-service] 30 citas creadas con Faker       ");
        System.out.println("=====================================================");
    }
}
