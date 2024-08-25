package com.corvinicolas.bus_logger_notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BusLoggerNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusLoggerNotifierApplication.class, args);
	}

}
