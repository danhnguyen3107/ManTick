package com.med.system.ManTick;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.med.system.ManTick.auth.AuthenticationService;
import com.med.system.ManTick.auth.RegisterRequest;
import static com.med.system.ManTick.Users.Role.*;


@SpringBootApplication()
public class ManTickApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManTickApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return _ -> {
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
		};
	}
}
