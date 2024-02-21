package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.repository.HotelRepository;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final String LOCATION = "location";

    private final String IMAGES = "images";

    private final String AMENITIES = "amenities";

    private final HotelRepository hotelRepository;

    @Override
    public List<Hotel> findById(List<String> ids){
        return hotelRepository.findAllById(ids);
    }

    @Override
    public Hotel findByDestinationId(Integer destinationIds){
        return hotelRepository.findByLocation_DestinationId(destinationIds);
    }

    @Override
    public Hotel convertData(JsonElement element, String mapper){
        Hotel hotel = new Hotel();
        JsonObject supplierObject = element.getAsJsonObject();
        JsonObject mapperObject = JsonParser.parseString(mapper).getAsJsonObject();
        JsonObject hotelObject = new JsonObject();

        mapperObject.entrySet().forEach(entry -> {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().toString().replace("\"","");
            if (fieldName.startsWith(LOCATION)){
                setLocationData(hotelObject, supplierObject, fieldName, fieldValue);
                return;
            }
            if (fieldName.startsWith(AMENITIES)){
                setAmenityData(hotelObject, supplierObject, fieldName, fieldValue);
                return;
            }
            if (fieldName.startsWith(IMAGES)){
                setImageData(hotelObject, supplierObject, fieldName, fieldValue);
                return;
            }
            hotelObject.add(fieldName, supplierObject.get(fieldValue));
        });
        log.info("Result: {}", hotelObject);
        return hotel;
    }

    public JsonElement getLocationValue(JsonObject supplierObject, String supplierFieldName){
        if (supplierFieldName.contains("\\.")){
            JsonObject locationObject = supplierObject.get(LOCATION).getAsJsonObject();
            return locationObject.get(supplierFieldName);
        }
       return supplierObject.get(supplierFieldName);

    }

    public void setLocationData(JsonObject hotelObject, JsonObject supplierObject, String fieldName, String fieldValue){
        String[] properties = fieldName.split("\\.");
        String childProperty = properties[1];
        if (hotelObject.has(LOCATION)){
            JsonObject object = hotelObject.get(LOCATION).getAsJsonObject();
            object.add(childProperty, getLocationValue(supplierObject, fieldValue));
            getLocationValue(supplierObject, fieldValue);
        } else {
            JsonObject object = new JsonObject();
            object.add(childProperty, getLocationValue(supplierObject, fieldValue));
            hotelObject.add(LOCATION, object);
        }
    }
    public void setAmenityData(JsonObject hotelObject, JsonObject supplierObject, String fieldName, String fieldValue){
        return;
    }

    public void setImageData(JsonObject hotelObject, JsonObject supplierObject, String fieldName, String fieldValue){
        return;
    }
}
