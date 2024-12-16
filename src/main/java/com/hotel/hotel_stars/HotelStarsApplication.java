package com.hotel.hotel_stars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelStarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelStarsApplication.class, args);
    }

}
