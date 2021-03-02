package edu.uta.cse6331.assignment01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableCaching
public class Assignment01Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment01Application.class, args);
	}

}
