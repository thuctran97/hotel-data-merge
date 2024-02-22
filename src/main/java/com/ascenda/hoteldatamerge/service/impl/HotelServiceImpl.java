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

    private static final String LOCATION = "location";

    private static final String IMAGES = "images";

    private static final String AMENITIES = "amenities";

    private static final String URL = "url";

    private static final String NAME = "name";

    private static final String TYPE = "type";

    private static final String DESCRIPTION = "description";

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
    public Hotel convertData(JsonObject supplierObject, String mapperSchema){
        JsonObject mapperObject = JsonParser.parseString(mapperSchema).getAsJsonObject();
        JsonObject hotelObject = new JsonObject();

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
        JsonObject locationObject = new JsonObject();
        for (String locationKey: locationMapperObject.keySet()) {
            String supplierKey = getValueFromJsonObject(locationMapperObject, locationKey);
            if (supplierKey.contains(".")){
                String supplierKey2 = supplierKey.split("\\.")[1];
                JsonObject supplierLocationObject = supplierObject.get(LOCATION).getAsJsonObject();
                locationObject.add(locationKey, supplierLocationObject.get(supplierKey2));
                return;
            }
            locationObject.add(locationKey, supplierObject.get(supplierKey));
        }
        hotelObject.add(LOCATION, locationObject);
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
