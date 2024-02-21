package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.repository.HotelRepository;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.google.gson.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final String LOCATION = "location";

    private final String IMAGES = "images";

    private final String AMENITIES = "amenities";

    private final String[] specielFields = new String[]{LOCATION, IMAGES, AMENITIES};

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
        Arrays.stream(specielFields).forEach(field -> hotelObject.add(field, new JsonObject()));
        mapperObject.entrySet().forEach(entry -> {
            String fieldName = entry.getKey();
            if (LOCATION.equals(fieldName)){
                setLocationData(hotelObject, supplierObject, entry.getValue().getAsJsonObject());
                return;
            }
            if (AMENITIES.equals(fieldName)){
                setAmenityData(hotelObject, supplierObject, entry.getValue().getAsJsonObject());
                return;
            }
            if (IMAGES.equals(fieldName)){
                setImageData(hotelObject, supplierObject, entry.getValue().getAsJsonObject());
                return;
            }
            hotelObject.add(fieldName, supplierObject.get(entry.getValue().toString().replace("\"","")));
        });
        log.info("Result: {}", hotelObject);
        return hotel;
    }

    public void setLocationData(JsonObject hotelObject, JsonObject supplierObject, JsonObject locationMapperObject){
        JsonObject locationResultObject = hotelObject.get(LOCATION).getAsJsonObject();
        for (String locationKey: locationMapperObject.keySet()) {
            String locationValue = locationMapperObject.get(locationKey).toString().replace("\"","");
            if (locationValue.contains("\\.")){
                String locationChildFieldKey = locationValue.split("\\.")[1];
                JsonObject locationSupplierObject = supplierObject.get(LOCATION).getAsJsonObject();
                locationResultObject.add(locationKey, locationSupplierObject.get(locationChildFieldKey));
                return;
            }
            locationResultObject.add(locationKey, supplierObject.get(locationValue));
        }
    }

    public void setAmenityData(JsonObject hotelObject, JsonObject supplierObject, JsonObject amenityMapperObject){
        JsonElement nameMapperKey = amenityMapperObject.get("name");
        JsonElement typeMapperKey = amenityMapperObject.get("type");
        if (AMENITIES.equals(nameMapperKey.toString())){
            List<JsonElement> amenityList = supplierObject.get(AMENITIES).getAsJsonArray().asList();
            JsonArray amenityArray = new JsonArray();
            amenityList.forEach(amenity -> {
                JsonObject object = new JsonObject();
                object.add("name", amenity);
                object.add("type", typeMapperKey);
                amenityArray.add(object);
            });
            hotelObject.add(AMENITIES, amenityArray);
        } else {
            JsonObject amenityObject = supplierObject.get(AMENITIES).getAsJsonObject();
            for (String amenityType: amenityObject.keySet()) {
                List<JsonElement> amenityList = amenityObject.get(amenityType).getAsJsonArray().asList();
                JsonArray amenityArray = new JsonArray();
                amenityList.forEach(amenity -> {
                    JsonObject object = new JsonObject();
                    object.add("name", amenity);
                    object.addProperty("type", amenityType);
                    amenityArray.add(object);
                });
                hotelObject.add(AMENITIES, amenityArray);
            }
        }
    }

    public void setImageData(JsonObject hotelObject, JsonObject supplierObject, JsonObject imageMapperObject){
    }
}
