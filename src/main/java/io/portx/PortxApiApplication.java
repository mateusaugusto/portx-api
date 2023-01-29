package io.portx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PortxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortxApiApplication.class, args);
	}

}
