package com.example.server;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.server.model.Task;
import com.example.server.model.TaskStatus;
import com.example.server.model.ImportanceLevel;
import com.example.server.repository.TaskRepository;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepo;


	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(TaskRepository taskRepo) {
		return args -> {


		};
	}

	@Override
	public void run(String... args) {

	}
}
