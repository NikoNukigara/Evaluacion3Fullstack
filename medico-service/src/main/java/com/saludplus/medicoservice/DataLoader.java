package com.saludplus.medicoservice;

import com.saludplus.medicoservice.model.Medico;
import com.saludplus.medicoservice.repository.MedicoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        if (medicoRepository.count() > 0) {
            return;
        }

        List<String> especialidades = Arrays.asList("Cardiología", "Pediatría", "Medicina General", "Dermatología", "Neurología", "Ginecología", "Traumatología");

        for (int i = 0; i < 10; i++) {
            Medico medico = new Medico();
            
            medico.setNombre("Dr. " + faker.name().fullName());
            medico.setEspecialidad(especialidades.get(random.nextInt(especialidades.size())));
            medico.setTelefono(faker.phoneNumber().cellPhone());
            medico.setCorreo(faker.internet().emailAddress());
            
            medicoRepository.save(medico);
        }
        System.out.println("=====================================================");
        System.out.println("   [medico-service] 10 médicos creados con Faker     ");
        System.out.println("=====================================================");
    }
}
