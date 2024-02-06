package org.mindswap.academy.mindera_travel_agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MinderaTravelAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinderaTravelAgencyApplication.class, args);
    }
}
