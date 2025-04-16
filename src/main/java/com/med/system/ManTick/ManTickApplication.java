package com.med.system.ManTick;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.auth.AuthenticationService;
import com.med.system.ManTick.auth.RegisterRequest;
import static com.med.system.ManTick.Users.Role.*;


@SpringBootApplication()
@EnableAsync  
public class ManTickApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManTickApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			UserRepository repository
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@gmail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("manager@gmail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

			var user = RegisterRequest.builder()
					.firstname("user")
					.lastname("user")
					.email("user@gmail.com")
					.password("password")
					.role(USER)
					.build();
			System.out.println("Manager token: " + service.register(user).getAccessToken());


			var trailingUser = RegisterRequest.builder()
					.firstname("trailingUser")
					.lastname("trailingUser")
					.email("trailingUser@gmail.com")
					.password("password")
					.role(USER)
					.build();
			System.out.println("Manager token: " + service.register(trailingUser).getAccessToken());

			var chatAi = RegisterRequest.builder()
				.firstname("chatAi")
				.lastname("chatAi")
				.email("chatAi@gmail.com")
				.password("password")
				.role(USER)
				.build();
			System.out.println("Manager token: " + service.register(chatAi).getAccessToken());

			User chatAiUser = repository.findByEmail("chatAi@gmail.com").orElseThrow( () -> new RuntimeException("User not found"));
			chatAiUser.setAI(true);
			repository.save(chatAiUser);
		};
	}
}
