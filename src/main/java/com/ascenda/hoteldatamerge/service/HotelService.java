package com.ascenda.hoteldatamerge.service;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.google.gson.JsonElement;

public interface HotelService {
    Hotel convertData(JsonElement element, String mapper);
}
