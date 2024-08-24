package com.bikeride.Service;

import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Model.User;
import com.bikeride.Model.Request.Userrequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getuser(Userrequest userrequest);
    BikeRideResponse registeruser(Userrequest userrequest);
    BikeRideResponse forgotPasswordOrUsername(Userrequest userrequest);
}
