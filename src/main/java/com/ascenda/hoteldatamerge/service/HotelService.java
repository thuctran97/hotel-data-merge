package com.ascenda.hoteldatamerge.service;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.google.gson.JsonElement;

import java.util.List;

public interface HotelService {
    Hotel convertData(JsonElement element, String mapper);

    List<Hotel> findById(List<String> id);

    Hotel findByDestinationId(Integer destinationId);
}
