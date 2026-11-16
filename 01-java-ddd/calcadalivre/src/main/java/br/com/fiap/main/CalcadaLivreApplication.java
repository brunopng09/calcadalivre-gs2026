package br.com.fiap.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicacao CalcaLivre.
 * Sobe o contexto Spring Boot e expoe a API REST e as paginas Thymeleaf.
 */
@SpringBootApplication(scanBasePackages = "br.com.fiap")
public class CalcadaLivreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalcadaLivreApplication.class, args);
    }
}
