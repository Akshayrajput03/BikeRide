package com.bikeride.Controller;

import com.bikeride.Model.Request.AddUserToRide;
import com.bikeride.Model.Request.JoinRideRequest;
import com.bikeride.Model.Request.PrivateRideRequest;
import com.bikeride.Model.Request.RideRequest;
import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Service.RideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
@Slf4j
public class RideController {

    @Autowired
    RideService rideService;

    @PostMapping("/create")
    public ResponseEntity<BikeRideResponse> createRide(@RequestBody RideRequest rideRequest){
        log.info("RideController:createRide {}",rideRequest);
        BikeRideResponse bikeRideResponse=rideService.createRide(rideRequest);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @GetMapping("/publicRide")
    public ResponseEntity getPublicRide(){
        log.info("RideController:getPublicRide {}");
        BikeRideResponse bikeRideResponse=rideService.getPublicRide();
        log.info("Response: {}",bikeRideResponse);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/privateRide")
    public ResponseEntity getPrivateRide(@RequestBody PrivateRideRequest privateRideRequest){
        log.info("RideController:getPrivateRide {}",privateRideRequest.getUserId());
        BikeRideResponse bikeRideResponse=rideService.getPrivateRide(privateRideRequest.getUserId());
        log.info("RideController:getPrivateRide response {}",bikeRideResponse);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/joinRide")
    public ResponseEntity joinRide(@RequestBody JoinRideRequest joinRideRequest){
        log.info("RideController:joinRide {}",joinRideRequest);
        BikeRideResponse bikeRideResponse=rideService.joinRide(joinRideRequest);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody AddUserToRide addUserToRide){
        log.info("RideController:addUser {}",addUserToRide);
        BikeRideResponse bikeRideResponse=rideService.addUser(addUserToRide);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/removeUser")
    public ResponseEntity removeUser(@RequestBody AddUserToRide addUserToRide){
        log.info("RideController:removeUser {}",addUserToRide);
        BikeRideResponse bikeRideResponse=rideService.removeUser(addUserToRide);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/viewRide")
    public ResponseEntity rideDetails(@RequestBody RideRequest rideRequest){
        log.info("RideController:rideDetails {}",rideRequest);
        BikeRideResponse bikeRideResponse=rideService.rideDetails(rideRequest);
        log.info("RideController:rideDetails response {}",bikeRideResponse);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/updateRide")
    public ResponseEntity updateRide(@RequestBody RideRequest rideRequest){
        log.info("RideController:updateRide {}",rideRequest);
        BikeRideResponse bikeRideResponse=rideService.updateRide(rideRequest);
        log.info("RideController:updateRide response {}",bikeRideResponse);
        return ResponseEntity.ok().body(bikeRideResponse);
    }
}
