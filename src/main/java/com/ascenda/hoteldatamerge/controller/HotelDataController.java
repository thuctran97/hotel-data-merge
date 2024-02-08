package com.ascenda.hoteldatamerge.controller;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.service.SupplierService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/hotel-data")
@Slf4j
@RequiredArgsConstructor
public class HotelDataController {

    private final SupplierService supplierService;

    @GetMapping ("/")
    public Hotel getHotelDataById(@RequestParam List<String> id) {
        log.info("GET HOTEL DATA USING ID LIST: {}", id);
        return null;
    }

    @GetMapping ("/")
    public Hotel getHotelDataByDestinationId(@RequestParam String destinationId) {
        log.info("GET HOTEL DATA USING DESTINATION ID: {}", destinationId);
        return null;
    }

}
