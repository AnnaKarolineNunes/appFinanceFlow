package com.nunes.financeFlow;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceFlowApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();  // Carrega o arquivo .env

		//acessar as variáveis do .env
		String dbUrl = dotenv.get("DATABASE_URL");
		String dbUser = dotenv.get("DATABASE_USER");
		String dbPassword = dotenv.get("DATABASE_PASSWORD");
		String jwtsecret = dotenv.get("JWT_SECRET");
		String expiration_hours = dotenv.get("EXPIRATION_HOURS");

		System.setProperty("api.security.token.expiration.hours" , expiration_hours);
		System.setProperty("spring.datasource.url", dbUrl);
		System.setProperty("spring.datasource.username", dbUser);
		System.setProperty("spring.datasource.password", dbPassword);
		System.setProperty("api.security.token.secret", jwtsecret);  // Configura a secret key do JWT

		SpringApplication.run(FinanceFlowApplication.class, args);
	}

}
