package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.repository.HotelRepository;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.google.gson.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final String LOCATION = "location";

    private final String IMAGES = "images";

    private final String AMENITIES = "amenities";

    private final String URL = "url";

    private final String NAME = "name";

    private final String TYPE = "type";

    private final String DESCRIPTION = "description";

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
        JsonObject supplierObject = element.getAsJsonObject();
        JsonObject mapperObject = JsonParser.parseString(mapper).getAsJsonObject();
        JsonObject hotelObject = new JsonObject();
        Arrays.stream(specielFields).forEach(field -> hotelObject.add(field, new JsonObject()));
        mapperObject.entrySet().forEach(entry -> {
            String fieldName = entry.getKey();
            JsonElement fieldValue = entry.getValue();
            if (LOCATION.equals(fieldName)){
                setLocationData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
                return;
            }
            if (AMENITIES.equals(fieldName)){
                setAmenityData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
                return;
            }
            if (IMAGES.equals(fieldName)){
                setImageData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
                return;
            }
            hotelObject.add(fieldName, supplierObject.get(fieldValue.toString()));
        });
        return new Gson().fromJson(hotelObject, Hotel.class);
    }

    public String getValueFromJsonObject(JsonObject jsonObject, String key){
        String value = jsonObject.get(key).toString();
        return value.replace("\"","");
    }

    public void setLocationData(JsonObject hotelObject, JsonObject supplierObject, JsonObject locationMapperObject){
        JsonObject locationResultObject = hotelObject.get(LOCATION).getAsJsonObject();
        for (String locationKey: locationMapperObject.keySet()) {
            String locationValue = getValueFromJsonObject(locationMapperObject, locationKey);
            if (locationValue.contains(".")){
                String locationChildFieldKey = locationValue.split("\\.")[1];
                JsonObject locationSupplierObject = supplierObject.get(LOCATION).getAsJsonObject();
                locationResultObject.add(locationKey, locationSupplierObject.get(locationChildFieldKey));
                return;
            }
            locationResultObject.add(locationKey, supplierObject.get(locationValue));
        }
    }

    public void setAmenityData(JsonObject hotelObject, JsonObject supplierObject, JsonObject amenityMapperObject){
        String nameMapperKey = getValueFromJsonObject(amenityMapperObject, NAME);
        String typeMapperKey = getValueFromJsonObject(amenityMapperObject, TYPE);

        if (AMENITIES.equals(nameMapperKey)){
            List<JsonElement> amenityList = supplierObject.get(AMENITIES).getAsJsonArray().asList();
            JsonArray amenityArray = new JsonArray();
            amenityList.forEach(amenity -> {
                JsonObject object = new JsonObject();
                object.add(NAME, amenity);
                object.addProperty(TYPE, typeMapperKey);
                amenityArray.add(object);
            });
            hotelObject.add(AMENITIES, amenityArray);
        } else {
            JsonObject amenityObject = supplierObject.get(AMENITIES).getAsJsonObject();
            JsonArray amenityArray = new JsonArray();
            for (String amenityType: amenityObject.keySet()) {
                List<JsonElement> amenityList = amenityObject.get(amenityType).getAsJsonArray().asList();
                amenityList.forEach(amenity -> {
                    JsonObject object = new JsonObject();
                    object.add(NAME, amenity);
                    object.addProperty(TYPE, amenityType);
                    amenityArray.add(object);
                });
            }
            hotelObject.add(AMENITIES, amenityArray);
        }
    }

    public void setImageData(JsonObject hotelObject, JsonObject supplierObject, JsonObject imageMapperObject){
        String urlMapperKey = getValueFromJsonObject(imageMapperObject, URL);
        String descriptionMapperKey = getValueFromJsonObject(imageMapperObject, DESCRIPTION);

        String urlMapper = urlMapperKey.split("\\.")[2];
        String descriptionMapper = descriptionMapperKey.split("\\.")[2];

        JsonObject imageObject = supplierObject.get(IMAGES).getAsJsonObject();
        JsonArray imageArray = new JsonArray();
        for (String imageType: imageObject.keySet()) {
            List<JsonElement> imageList = imageObject.get(imageType).getAsJsonArray().asList();
            imageList.forEach(image -> {
                JsonObject object = new JsonObject();
                object.add(URL, image.getAsJsonObject().get(urlMapper));
                object.add(DESCRIPTION, image.getAsJsonObject().get(descriptionMapper));
                object.addProperty(TYPE, imageType);
                imageArray.add(object);
            });
        }
        hotelObject.add(IMAGES, imageArray);
    }
}
