package com.ascenda.hoteldatamerge.scheduler;


import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

    private final SupplierService supplierService;

    @Scheduled(cron = "*/10 * * * * ?")
    public void doDataLoader() {
        log.info("Cron Task");
        List<Supplier> supplierList = supplierService.getAllSuppliers();
        for (Supplier sup: supplierList) {
            log.info("Location: {}", sup.getMappingSchema().toString());
        }
    }

}