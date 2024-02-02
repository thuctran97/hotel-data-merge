package com.ascenda.hoteldatamerge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class HotelDataMergeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelDataMergeApplication.class, args);
    }

}
