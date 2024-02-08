package com.ascenda.hoteldatamerge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hotels")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    private String id;

    private String name;

    private String description;

    private String[] facilities;

    private String[] amenities;

    private Location location;

    private Image image;
}
