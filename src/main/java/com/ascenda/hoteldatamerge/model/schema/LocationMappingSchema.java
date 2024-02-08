package com.ascenda.hoteldatamerge.model.schema;

import lombok.*;

@Getter
public class LocationMappingSchema {
    private String destinationId;

    private String address;

    private String country;

    private String latitude;

    private String longitude;
}
