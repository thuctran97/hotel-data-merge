package com.ascenda.hoteldatamerge.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.service.DatalakeService;
import com.ascenda.hoteldatamerge.service.HotelService;
import com.ascenda.hoteldatamerge.service.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataProcessingJob {

	private final SupplierService supplierService;

	private final HotelService hotelService;

	private final DatalakeService datalakeService;

	@Scheduled(cron = "*/10 * * * * ?")
	public void doDataProcessing() {
		log.info("START DATA EXTRACTING + LOADING");
		List<Supplier> supplierList = supplierService.getAllSuppliers();
		supplierService.extractAndLoadData(supplierList);
		log.info("END DATA EXTRACTING + LOADING");

		Map<Integer, String> mappingMap = getMappingMap(supplierList);
		List<String> hotelData = datalakeService.getAllDocuments();

		log.info("START DATA TRANSFORMING");
		hotelService.transformData(hotelData, mappingMap);
		datalakeService.clearCollection();
		log.info("END DATA TRANSFORMING");
	}

	public Map<Integer, String> getMappingMap(List<Supplier> supplierList) {
		Map<Integer, String> result = new HashMap<>();
		supplierList.forEach(supplier -> result.put(supplier.getDataPriorityLevel(), supplier.getMappingSchema()));
		return result;
	}

}