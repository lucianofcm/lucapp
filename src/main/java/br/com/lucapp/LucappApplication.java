package br.com.lucapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"br.com.lucapp"})
@EntityScan("br.com.lucapp")
@EnableWebMvc
public class LucappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LucappApplication.class, args);
	}

}
