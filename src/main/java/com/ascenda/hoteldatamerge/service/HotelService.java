package com.ascenda.hoteldatamerge.service;

import java.util.List;
import java.util.Map;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.google.gson.JsonObject;

public interface HotelService {
	void transformData(List<String> supplierDataList, Map<Integer, String> referenceMap);

	List<Hotel> findAllById(List<String> id);

	Hotel findById(String id);

	List<Hotel> findByDestinationId(Integer destinationId);
}
