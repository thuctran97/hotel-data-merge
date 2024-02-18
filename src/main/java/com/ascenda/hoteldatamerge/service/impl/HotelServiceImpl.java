package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {

    @Override
    public Hotel convertData(JsonElement element, String mapper){
        Hotel hotel = new Hotel();
        JsonObject supplierDataObject = element.getAsJsonObject();
        JsonObject mapperObject = JsonParser.parseString(mapper).getAsJsonObject();
        JsonObject hotelObject = new JsonObject();

        mapperObject.entrySet().forEach(entry -> {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().toString().replace("\"","");
            if (fieldName.contains(".")){
                String[] properties = fieldName.split("\\.");
                String fatherProperty = properties[0];
                String childProperty = properties[1];
                if (hotelObject.has(fatherProperty)){
                    JsonObject object = hotelObject.get(fatherProperty).getAsJsonObject();
                    object.add(childProperty, supplierDataObject.get(fieldValue));
                } else {
                    JsonObject object = new JsonObject();
                    object.add(childProperty, supplierDataObject.get(fieldValue));
                    hotelObject.add(fatherProperty, object);
                }
            } else {
                hotelObject.add(fieldName, supplierDataObject.get(fieldValue));
            }
        });
        log.info("Result: {}", hotelObject);
        return hotel;
    }
}
