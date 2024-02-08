package com.ascenda.hoteldatamerge.model.mapper;

import lombok.Getter;

@Getter
public class HotelMapper {

    private String id;

    private String name;

    private String description;

    private String facilities;

    private String amenities;

    private LocationMapper location;

    private ImageMapper image;
}
