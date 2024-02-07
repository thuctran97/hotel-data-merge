package com.ascenda.hoteldatamerge.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader {

    @Scheduled(cron = "*/10 * * * * ?")
    public void doDataLoader() {
        log.info("Cron Task");
    }

}