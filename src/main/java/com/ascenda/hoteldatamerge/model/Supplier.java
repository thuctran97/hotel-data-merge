package com.ascenda.hoteldatamerge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "suppliers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

	@Id
	private String id;

	private String name;

	private String url;

	private Integer dataPriorityLevel;

	private String mappingSchema;
}
