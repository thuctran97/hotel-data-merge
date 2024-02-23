package com.ascenda.hoteldatamerge.scheduler;

import static com.ascenda.hoteldatamerge.constant.StringConstant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.service.DatalakeService;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.ascenda.hoteldatamerge.service.SupplierService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

	private final SupplierService supplierService;

	private final HotelService hotelService;

	private final DatalakeService datalakeService;

	@Scheduled(cron = "*/5 * * * * ?")
	public void doDataLoader() {
		log.info("START DATA LOADER");
		List<Supplier> supplierList = supplierService.getAllSuppliers();
		supplierService.extractAndInsertData(supplierList);

		Map<Integer, String> mappingMap = getMappingMap(supplierList);
		List<String> hotelData = datalakeService.getAllDocuments();
		log.info("DOCUMENTS SIZE: {}", hotelData.size());

		transformData(hotelData, mappingMap);
		datalakeService.clearCollection();
	}

	public JsonObject convertToJsonObject(String input) {
		return JsonParser.parseString(input).getAsJsonObject();
	}

	public void transformData(List<String> supplierDataMap, Map<Integer, String> mappingMap) {
		supplierDataMap.forEach(supplierData -> {
			JsonObject supplierObject = convertToJsonObject(supplierData);
			Integer priorityLevel = supplierObject.get(PRIORITY_LEVEL).getAsInt();
			JsonObject schemaObject = convertToJsonObject(mappingMap.get(priorityLevel));
			Hotel hotel = hotelService.convertData(supplierObject, schemaObject);
			log.info("HOTEL: {}", hotel);
		});
	}

	public Map<Integer, String> getMappingMap(List<Supplier> supplierList) {
		Map<Integer, String> result = new HashMap<>();
		supplierList.forEach(supplier -> result.put(supplier.getDataPriorityLevel(), supplier.getMappingSchema()));
		return result;
	}

}