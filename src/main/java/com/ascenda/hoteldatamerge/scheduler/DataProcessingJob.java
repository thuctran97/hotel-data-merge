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

	@Scheduled(cron = "*/30 * * * * ?")
	public void doDataProcessing() {
		doDataExtractingAndLoading();
		doDataTransforming();
	}

	public void doDataExtractingAndLoading(){
		log.info("START DATA EXTRACTING + LOADING JOB");

		supplierService.extractAndLoadData(supplierService.getAllSuppliers());

		log.info("END DATA EXTRACTING + LOADING JOB");
	}

	public void doDataTransforming(){
		log.info("START DATA TRANSFORMING");

		Map<Integer, String> referenceMap = getMappingMap(supplierService.getAllSuppliers());
		List<String> supplierDataList = datalakeService.getAllDocuments();
		hotelService.transformData(supplierDataList, referenceMap);
		datalakeService.clearCollection();

		log.info("END DATA TRANSFORMING JOB");
	}

	public Map<Integer, String> getMappingMap(List<Supplier> supplierList) {
		Map<Integer, String> result = new HashMap<>();
		supplierList.forEach(supplier -> result.put(supplier.getPriorityLevel(), supplier.getReferenceSchema()));
		return result;
	}

}