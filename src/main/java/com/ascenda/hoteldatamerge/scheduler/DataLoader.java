package com.ascenda.hoteldatamerge.scheduler;


import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.repository.HotelRepository;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.ascenda.hoteldatamerge.service.SupplierService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

    private static final String DATA_LAKE_COLLECTION = "data-lake";

    private static final String PRIORITY_LEVEL = "priorityLevel";

    private final SupplierService supplierService;

    private final HotelService hotelService;

    private final RestTemplate restTemplate;

    private final MongoTemplate mongoTemplate;

    private final HotelRepository hotelRepository;

    @Scheduled(cron = "*/5 * * * * ?")
    public void doDataLoader() {
        log.info("START DATA LOADER");
        List<Supplier> supplierList = supplierService.getAllSuppliers();
        supplierList.forEach(supplier -> {
            ResponseEntity<String> response = restTemplate.getForEntity(supplier.getUrl(), String.class);
            JsonArray jsonArray = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonArray();
            jsonArray.asList().stream().map(JsonElement::getAsJsonObject).forEach(jsonObject -> {
                jsonObject.add(PRIORITY_LEVEL, JsonParser.parseString(supplier.getDataPriorityLevel().toString()));
                mongoTemplate.save(jsonObject.toString(), DATA_LAKE_COLLECTION);
            });
        });

        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, PRIORITY_LEVEL));
        List<Map> documents = mongoTemplate.find(query, Map.class, DATA_LAKE_COLLECTION);
        log.info("DOCUMENTS SIZE: {}", documents.size());
        mongoTemplate.remove(new Query(), DATA_LAKE_COLLECTION);
    }

}