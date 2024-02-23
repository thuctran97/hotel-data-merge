package com.ascenda.hoteldatamerge.service;

import java.util.List;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.google.gson.JsonObject;

public interface HotelService {
	Hotel convertData(JsonObject supplierObject, JsonObject mapperObject);

	List<Hotel> findById(List<String> id);

	Hotel findByDestinationId(Integer destinationId);
}
