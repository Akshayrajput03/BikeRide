package com.bikeride.Model.Request;

import lombok.Data;

@Data
public class JoinRideRequest {

    private String rideId;
    private String userId; // user_id of user who wants to join ride
}
