package com.bikeride.Service.impl;

import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Model.User;
import com.bikeride.Model.Request.Userrequest;
import com.bikeride.Repository.UserRepository;
import com.bikeride.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSerivceImpl implements UserService {

    private final UserRepository userRepository;

    public UserSerivceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getuser(Userrequest userrequest) {
        return userRepository.getUser(userrequest);
    }

    @Override
    public BikeRideResponse registeruser(Userrequest userrequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        User us=userRepository.checkForUserName(userrequest.getUsername());
        if(us==null){
            log.info("User is null with username:{}",userrequest.getUsername());
             userRepository.insertUser(userrequest);
            bikeRideResponse.setResStatus("Created");
            bikeRideResponse.setResCode("2000");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setErrorDesc("User Already Registered");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse forgotPasswordOrUsername(Userrequest userrequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        User us=userRepository.checkForUserName(userrequest.getUsername());
        if(us==null){
            bikeRideResponse.setResCode("4002");
            bikeRideResponse.setErrorDesc("UserName not Registered");
            return bikeRideResponse;
        }
        bikeRideResponse.setResCode("2001");
        bikeRideResponse.setErrorDesc("UserName is present");
        return bikeRideResponse;
    }
}
