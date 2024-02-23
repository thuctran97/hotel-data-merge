package com.ascenda.hoteldatamerge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUrl;

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUrl));
	}
}
