package com.ascenda.hoteldatamerge.service.impl;

import static com.ascenda.hoteldatamerge.constant.StringConstant.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.repository.HotelRepository;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	private final Field[] hotelFields = Hotel.class.getDeclaredFields();

	private final HotelRepository hotelRepository;

	@Override
	public List<Hotel> findAllById(List<String> ids) {
		return hotelRepository.findAllById(ids);
	}

	@Override
	public Hotel findById(String id) {
		Optional<Hotel> hotel = hotelRepository.findById(id);
		if (hotel.isEmpty()) {
			return null;
		}
		return hotel.get();
	}

	@Override
	public List<Hotel> findByDestinationId(Integer destinationId) {
		return hotelRepository.findByLocation_DestinationId(destinationId);
	}

	public void insertData(Hotel hotel) {
		Hotel existingHotel = findById(hotel.getId());
		if (existingHotel == null) {
			hotelRepository.insert(hotel);
			return;
		}

		for (Field field : hotelFields) {
			field.setAccessible(true);
			try {
				Object valueA = field.get(existingHotel);
				if (valueA == null) {
					Object valueB = field.get(hotel);
					field.set(existingHotel, valueB);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		hotelRepository.deleteById(existingHotel.getId());
        hotelRepository.insert(existingHotel);
	}

	@Override
	public void transformData(List<String> supplierDataMap, Map<Integer, String> mappingMap) {
		supplierDataMap.forEach(supplierData -> {
			JsonObject supplierObject = convertToJsonObject(supplierData);
			Integer priorityLevel = supplierObject.get(PRIORITY_LEVEL).getAsInt();
			JsonObject schemaObject = convertToJsonObject(mappingMap.get(priorityLevel));
			Hotel hotel = mapData(supplierObject, schemaObject);
			insertData(hotel);
		});
	}

	@Override
	public Hotel mapData(JsonObject supplierObject, JsonObject mapperObject) {
		JsonObject hotelObject = new JsonObject();

		mapperObject.entrySet().forEach(entry -> {
			String fieldName = entry.getKey();
			JsonElement fieldValue = entry.getValue();
			if (LOCATION.equals(fieldName)) {
				setLocationData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
				return;
			}
			if (AMENITIES.equals(fieldName)) {
				setAmenityData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
				return;
			}
			if (IMAGES.equals(fieldName)) {
				setImageData(hotelObject, supplierObject, fieldValue.getAsJsonObject());
				return;
			}
			addToObject(hotelObject, fieldName, supplierObject.get(fieldValue.getAsString()));
		});
		return new Gson().fromJson(hotelObject, Hotel.class);
	}

	public void addToObject(JsonObject object, String key, JsonElement value) {
		if (value.isJsonNull()) {
			return;
		}
		if (value.isJsonPrimitive() && value.getAsString().isEmpty()) {
			return;
		}
		object.add(key, value);
	}

	public void setLocationData(JsonObject hotelObject, JsonObject supplierObject, JsonObject locationMapperObject) {
		JsonObject locationObject = new JsonObject();
		locationMapperObject.keySet().forEach(locationKey -> {
			String supplierKey = locationMapperObject.get(locationKey).getAsString();
			if (supplierKey.contains(".")) {
				String supplierKey2 = supplierKey.split("\\.")[1];
				JsonObject supplierLocationObject = supplierObject.get(LOCATION).getAsJsonObject();
				addToObject(locationObject, locationKey, supplierLocationObject.get(supplierKey2));
				return;
			}
			addToObject(locationObject, locationKey, supplierObject.get(supplierKey));
		});
		hotelObject.add(LOCATION, locationObject);
	}

	public void setAmenityData(JsonObject hotelObject, JsonObject supplierObject, JsonObject amenityMapperObject) {
		String nameMapperKey = amenityMapperObject.get(NAME).getAsString();
		String typeMapperKey = amenityMapperObject.get(TYPE).getAsString();
		JsonElement amenityElement = supplierObject.get(AMENITIES);
		if (amenityElement.isJsonNull()) {
			return;
		}
		if (AMENITIES.equals(nameMapperKey)) {
			List<JsonElement> amenityList = amenityElement.getAsJsonArray().asList();
			JsonArray amenityArray = new JsonArray();
			amenityList.forEach(amenity -> {
				JsonObject object = new JsonObject();
				object.add(NAME, amenity);
				object.addProperty(TYPE, typeMapperKey);
				amenityArray.add(object);
			});
			hotelObject.add(AMENITIES, amenityArray);
		} else {
			JsonObject amenityObject = amenityElement.getAsJsonObject();
			JsonArray amenityArray = new JsonArray();
			for (String amenityType : amenityObject.keySet()) {
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

	public void setImageData(JsonObject hotelObject, JsonObject supplierObject, JsonObject imageMapperObject) {
		String urlMapperKey = imageMapperObject.get(URL).getAsString();
		String descriptionMapperKey = imageMapperObject.get(DESCRIPTION).getAsString();

		String urlMapper = urlMapperKey.split("\\.")[2];
		String descriptionMapper = descriptionMapperKey.split("\\.")[2];

		JsonObject imageObject = supplierObject.get(IMAGES).getAsJsonObject();
		JsonArray imageArray = new JsonArray();
		for (String imageType : imageObject.keySet()) {
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

	public JsonObject convertToJsonObject(String input) {
		return JsonParser.parseString(input).getAsJsonObject();
	}
}
