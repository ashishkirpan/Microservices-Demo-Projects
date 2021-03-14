package com.example.microservices.mynetflixeurekanamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyNetflixEurekaNamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNetflixEurekaNamingServerApplication.class, args);
	}

}
