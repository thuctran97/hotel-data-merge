package com.ascenda.hoteldatamerge.service;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.google.gson.JsonObject;

import java.util.List;

public interface HotelService {
    Hotel convertData(JsonObject supplierObject, JsonObject mapperObject);

    List<Hotel> findById(List<String> id);

    Hotel findByDestinationId(Integer destinationId);
}
