package uk.first_catering.kiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.first_catering.kiosk")
public class KioskApplication {

	public static void main(String[] args) {
		SpringApplication.run(KioskApplication.class, args);
	}

}
