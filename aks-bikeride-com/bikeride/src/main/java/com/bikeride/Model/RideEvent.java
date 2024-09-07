package com.bikeride.Model;

import lombok.Data;

import java.util.List;

@Data
public class RideEvent {

    private long id;
    private String rideType; //public private
    private String rideName;
    private String location;
    private String dateTime;
    private String userId;
    private String username;
    private List<User> user;
    private String mobileNumber;
    private String itenary;
}
