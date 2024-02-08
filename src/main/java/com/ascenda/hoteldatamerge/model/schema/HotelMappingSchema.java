package com.ascenda.hoteldatamerge.model.schema;

import lombok.Getter;

@Getter
public class HotelMappingSchema {

    private String id;

    private String name;

    private String description;

    private String facilities;

    private String amenities;

    private LocationMappingSchema location;

    private ImageMappingSchema image;
}
