package com.example.DepartmentalStoreCrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.DepartmentalStoreCrud")
@EnableScheduling
public class DepartmentalStoreCrudApplication {
	public static void main(final String[] args) {
		SpringApplication.run(DepartmentalStoreCrudApplication.class, args);
	}
}
