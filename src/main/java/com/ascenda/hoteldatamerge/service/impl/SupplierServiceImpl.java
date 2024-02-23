package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.repository.SupplierRepository;
import com.ascenda.hoteldatamerge.service.SupplierService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private static final String DATA_LAKE_COLLECTION = "data-lake";

    private static final String PRIORITY_LEVEL = "priorityLevel";

    private final MongoTemplate mongoTemplate;

    private final SupplierRepository supplierRepository;

    private final RestTemplate restTemplate;

    @Override
    public List<Supplier> getAllSuppliers(){
        return supplierRepository.findAll();
    }

    @Override
    public void extractAndInsertData(List<Supplier> supplierList){
        supplierList.parallelStream().forEach(supplier -> {
            ResponseEntity<String> response = restTemplate.getForEntity(supplier.getUrl(), String.class);
            if (response.getBody() == null){
                log.info("Failed to get data from suppliers: name: {}, url: {}", supplier.getName(), supplier.getUrl());
                return;
            }
            JsonArray jsonArray = JsonParser.parseString(response.getBody()).getAsJsonArray();
            jsonArray.asList().stream().map(JsonElement::getAsJsonObject).forEach(jsonObject -> {
                jsonObject.add(PRIORITY_LEVEL, JsonParser.parseString(supplier.getDataPriorityLevel().toString()));
                mongoTemplate.save(jsonObject.toString(), DATA_LAKE_COLLECTION);
            });
        });
    }
}
