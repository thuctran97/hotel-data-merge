package com.ascenda.hoteldatamerge.model;

import com.ascenda.hoteldatamerge.model.mapper.HotelMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private HotelMapper mappingSchema;
}
