package com.atalibdev;

import com.atalibdev.request.RegistrationRequest;
import com.atalibdev.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.atalibdev.entities.Role.*;

@SpringBootApplication
public class JwtSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtSpringBootApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            AuthenticationService authenticationService) {
        return args -> {
          var admin = RegistrationRequest.builder()
                  .firstname("Admin")
                  .lastname("admin")
                  .email("admin@gmail.com")
                  .password("password")
                  .role(ADMIN)
                  .build();
          System.err.println("Admin token: " + authenticationService.register(admin).getAccessToken());

          var manager = RegistrationRequest.builder()
                    .firstname("Manager")
                    .lastname("manager")
                    .email("manager@gmail.com")
                    .password("password")
                    .role(MANAGER)
                    .build();
          System.err.println("Manager token: " + authenticationService.register(manager).getAccessToken());

          var user = RegistrationRequest.builder()
                    .firstname("User")
                    .lastname("user")
                    .email("user@gmail.com")
                    .password("password")
                    .role(USER)
                    .build();
            System.err.println("User token: " + authenticationService.register(user).getAccessToken());

        };
    }
}
