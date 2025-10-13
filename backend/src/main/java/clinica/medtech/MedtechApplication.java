package clinica.medtech;

import clinica.medtech.model.User;
import clinica.medtech.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MedtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedtechApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserService service) {

		return args -> {
			service.register(User.builder()
							.username("user1")
							.email("user1@mail.com")
							.password("user1_password")
					.build());
			service.register(User.builder()
					.username("user2")
					.email("user2@mail.com")
					.password("user2_password")
					.build());
			service.register(User.builder()
					.username("user3")
					.email("user3@mail.com")
					.password("user3_password")
					.build());
		};
	}

}
