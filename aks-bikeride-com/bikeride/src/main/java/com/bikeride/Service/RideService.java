package com.bikeride.Service;


import com.bikeride.Model.Request.AddUserToRide;
import com.bikeride.Model.Request.JoinRideRequest;
import com.bikeride.Model.Request.RideRequest;
import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Model.RideEvent;

public interface RideService {

    BikeRideResponse createRide(RideRequest rideRequest);
    BikeRideResponse getPublicRide();
    BikeRideResponse getPrivateRide(String userId);
    BikeRideResponse joinRide(JoinRideRequest joinRideRequest);
    BikeRideResponse addUser(AddUserToRide addUserToRide);
    BikeRideResponse removeUser(AddUserToRide addUserToRide);
    BikeRideResponse rideDetails(RideRequest rideRequest);
    BikeRideResponse updateRide(RideRequest rideRequest);
    BikeRideResponse deleteRide(RideRequest rideRequest);
}
