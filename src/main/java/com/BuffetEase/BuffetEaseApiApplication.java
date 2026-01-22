package com.BuffetEase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuffetEaseApiApplication {

	public static void main(String[] args)
    {
        //Main application
        System.out.println("This is main file");
		SpringApplication.run(BuffetEaseApiApplication.class, args);
	}

}
