package com.ascenda.hoteldatamerge.service;

import java.util.List;

import com.ascenda.hoteldatamerge.model.Supplier;

public interface SupplierService {

	List<Supplier> getAllSuppliers();

	void extractAndInsertData(List<Supplier> supplierList);

}
