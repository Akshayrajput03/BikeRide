package com.bikeride.Model.Request;

import com.bikeride.Model.User;
import lombok.Data;

import java.util.List;

@Data
public class RideRequest {

    private String rideType; //public private
    private String rideName;
    private long userId;
    private String username;
    private String location;
    private String time;
    private String date;
    private List<User> user;
}
