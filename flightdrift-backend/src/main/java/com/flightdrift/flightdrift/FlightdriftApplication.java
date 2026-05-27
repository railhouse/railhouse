package com.flightdrift.flightdrift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

/*
 * Author: Jamius Siam
 * Since: 04/05/2026
 */
@SpringBootApplication
public class FlightdriftApplication {

	static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.systemDefault()));
		SpringApplication.run(FlightdriftApplication.class, args);
	}
}
