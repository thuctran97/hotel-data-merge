package com.ascenda.hoteldatamerge.service.impl;

import com.ascenda.hoteldatamerge.model.Supplier;
import com.ascenda.hoteldatamerge.repository.SupplierRepository;
import com.ascenda.hoteldatamerge.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public List<Supplier> getAllSuppliers(){
        return supplierRepository.findAll();
    }
}
