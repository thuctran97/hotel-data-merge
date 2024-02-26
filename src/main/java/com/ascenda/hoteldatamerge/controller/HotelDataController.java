package com.ascenda.hoteldatamerge.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ascenda.hoteldatamerge.model.Hotel;
import com.ascenda.hoteldatamerge.service.HotelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelDataController {

	private final HotelService hotelService;

	@GetMapping("/get")
	public List<Hotel> getHotelDataById(@RequestParam List<String> id) {
		log.info("GET HOTEL DATA USING ID LIST: {}", id);
		return hotelService.findAllById(id);
	}

	@GetMapping("/get/destination")
	public Hotel getHotelDataByDestinationId(@RequestParam String destinationId) {
		log.info("GET HOTEL DATA USING DESTINATION ID: {}", destinationId);
		return hotelService.findByDestinationId(Integer.parseInt(destinationId));
	}

}
