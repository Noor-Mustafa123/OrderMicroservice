package com.OrderService.projectFolder;

import org.springframework.boot.SpringApplication;

public class TestProjectFolderApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProjectFolderApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
