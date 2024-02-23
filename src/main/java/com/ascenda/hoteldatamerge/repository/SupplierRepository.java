package com.ascenda.hoteldatamerge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ascenda.hoteldatamerge.model.Supplier;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {

}