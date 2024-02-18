package com.ascenda.hoteldatamerge.scheduler;


import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.ascenda.hoteldatamerge.service.SupplierService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

    private final SupplierService supplierService;

    private final HotelService hotelService;

    private final RestTemplate restTemplate;

    private final MongoTemplate mongoTemplate;

//    @Scheduled(cron = "*/10 * * * * ?")
    public void doDataLoader() {
        log.info("Cron Task");
        List<Supplier> supplierList = supplierService.getAllSuppliers();
        supplierList.forEach(supplier -> {
            ResponseEntity<String> response = restTemplate.getForEntity(supplier.getUrl(), String.class);
            JsonArray jsonArray = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonArray();
            jsonArray.asList().forEach(jsonElement -> {
                Hotel hotel = hotelService.convertData(jsonElement, supplier.getMappingSchema());
                mongoTemplate.save(jsonElement.toString(), "datalake");
            });
        });
    }

}