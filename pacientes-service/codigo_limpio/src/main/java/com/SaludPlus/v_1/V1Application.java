package com.SaludPlus.v_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class V1Application {

	public static void main(String[] args) {
		SpringApplication.run(V1Application.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner initData(
			com.SaludPlus.v_1.repository.UserRepository userRepository,
			org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
		return args -> {
			// Revisa si el usuario admin NO existe para crearlo
			if (userRepository.findByUsername("admin").isEmpty()) {
				com.SaludPlus.v_1.model.User admin = new com.SaludPlus.v_1.model.User();
				admin.setUsername("admin");
				// Encriptamos la clave "1234" con BCrypt
				admin.setPassword(passwordEncoder.encode("1234"));

				// ¡AQUÍ ESTÁ LA LÍNEA MÁGICA QUE FALTABA! Le asignamos el rol:
				admin.setRole(com.SaludPlus.v_1.model.Role.ROLE_ADMIN);

				userRepository.save(admin);
				System.out.println("==========================================");
				System.out.println("  ¡USUARIO ADMIN CREADO EXITOSAMENTE!  ");
				System.out.println("==========================================");
			}
		};
	}
}