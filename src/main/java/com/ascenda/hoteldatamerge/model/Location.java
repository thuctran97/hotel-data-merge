package com.ascenda.hoteldatamerge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
	private Integer destinationId;

	private String address;

	private String city;

	private String country;

	private Float latitude;

	private Float longitude;
}
