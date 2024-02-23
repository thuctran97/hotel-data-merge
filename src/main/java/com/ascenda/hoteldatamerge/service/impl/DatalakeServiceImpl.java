package com.ascenda.hoteldatamerge.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ascenda.hoteldatamerge.service.DatalakeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatalakeServiceImpl implements DatalakeService {

	private static final String DATA_LAKE_COLLECTION = "data-lake";

	private static final String PRIORITY_LEVEL = "priorityLevel";

	private final MongoTemplate mongoTemplate;

	@Override
	public void clearCollection() {
		mongoTemplate.remove(new Query(), DATA_LAKE_COLLECTION);
	}

	@Override
	public List<String> getAllDocuments() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, PRIORITY_LEVEL));
		return mongoTemplate.find(query, String.class, DATA_LAKE_COLLECTION);
	}
}
