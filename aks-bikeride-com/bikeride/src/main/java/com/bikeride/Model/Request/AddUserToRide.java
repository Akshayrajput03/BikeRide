package com.bikeride.Model.Request;

import lombok.Data;

@Data
public class AddUserToRide {

    private String rideId;
    private String userId;
    private String adduserId;
    private String rideAddUserName;
}
