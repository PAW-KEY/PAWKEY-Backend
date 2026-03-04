package org.sopt.pawkey.backendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class PawkeyApplication {
	public static void main(String[] args) {
		SpringApplication.run(PawkeyApplication.class, args);
	}
}
