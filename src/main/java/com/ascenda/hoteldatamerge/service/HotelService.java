package com.ascenda.hoteldatamerge.service;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.model.mapper.HotelMapper;

public interface HotelService {
    Hotel convertData(String data, HotelMapper mapper);
}
