package com.ascenda.hoteldatamerge.service;

import java.util.List;

import com.ascenda.hoteldatamerge.model.Supplier;

public interface SupplierService {

	List<Supplier> getAllSuppliers();

	void extractAndLoadData(List<Supplier> supplierList);

}
