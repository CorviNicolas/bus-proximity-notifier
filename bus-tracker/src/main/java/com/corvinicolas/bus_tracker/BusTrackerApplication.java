package com.corvinicolas.bus_tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
public class BusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTrackerApplication.class, args);
	}

}
