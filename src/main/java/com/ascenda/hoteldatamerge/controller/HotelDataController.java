package com.ascenda.hoteldatamerge.controller;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelDataController {

    private final SupplierService supplierService;

    @GetMapping ("/get")
    public Hotel getHotelDataById(@RequestParam List<String> id) {
        log.info("GET HOTEL DATA USING ID LIST: {}", id);
        return null;
    }

    @GetMapping ("/get/destination")
    public Hotel getHotelDataByDestinationId(@RequestParam String destinationId) {
        log.info("GET HOTEL DATA USING DESTINATION ID: {}", destinationId);
        return null;
    }

}
