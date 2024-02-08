package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.model.mapper.HotelMapper;
import com.ascenda.hoteldatamerge.service.HotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {
    public Hotel convertData(String data, HotelMapper mapper){
        Hotel hotel = new Hotel();
        return hotel;
    }
}
