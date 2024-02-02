package com.ascenda.hoteldatamerge.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader {

    @Scheduled(fixedRate = 5000)
    public void scheduleTaskWithCronExpression() {
        log.info("Cron Task");
    }

}