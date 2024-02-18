package com.ascenda.hoteldatamerge.repository;


import com.ascenda.hoteldatamerge.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    Hotel findByLocation_DestinationId(Integer destinationId);
}