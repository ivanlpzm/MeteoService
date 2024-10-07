package com.smoke.meteoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.smoke.meteoservice"})
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class MeteoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeteoServiceApplication.class, args);
    }

}
