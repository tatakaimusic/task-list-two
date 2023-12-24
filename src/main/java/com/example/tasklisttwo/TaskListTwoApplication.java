package com.example.tasklisttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TaskListTwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskListTwoApplication.class, args);
	}

}
