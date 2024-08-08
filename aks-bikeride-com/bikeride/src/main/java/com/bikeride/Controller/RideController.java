package com.bikeride.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
public class RideController {

    @PostMapping("/create")
    public ResponseEntity createRide(){
        return ResponseEntity.ok().body("Created");
    }
}
