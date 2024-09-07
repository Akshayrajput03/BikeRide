package com.bikeride.Model.Request;

import com.bikeride.Model.User;
import lombok.Data;

import java.util.List;

@Data
public class RideRequest {

    private String rideId;
    private String rideType; //public private
    private String rideName;
    private long userId;
    private String username;
    private String location;
    private String time;
    private String date;
    private String mobileNumber;
    private String itenary;
    private List<User> user;
}
