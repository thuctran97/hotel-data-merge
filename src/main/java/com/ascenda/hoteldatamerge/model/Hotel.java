package com.ascenda.hoteldatamerge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	private Amenity[] amenities;

	private Location location;

	private Image[] images;

	private String[] bookingConditions;
}
