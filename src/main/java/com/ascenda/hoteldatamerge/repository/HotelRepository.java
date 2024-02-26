package com.ascenda.hoteldatamerge.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ascenda.hoteldatamerge.model.Hotel;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
	List<Hotel> findByLocation_DestinationId(Integer destinationId);
}