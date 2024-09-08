package com.bikeride.Service.impl;

import com.bikeride.Model.Request.AddUserToRide;
import com.bikeride.Model.Request.JoinRideRequest;
import com.bikeride.Model.Request.RideRequest;
import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Model.RideEvent;
import com.bikeride.Model.User;
import com.bikeride.Repository.RideRepository;
import com.bikeride.Service.RideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public BikeRideResponse createRide(RideRequest rideRequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        int ride=rideRepository.createRide(rideRequest);
        if(ride==1){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse getPublicRide() {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        List<RideEvent> rideEventsList=rideRepository.getPublicRide();
        if(rideEventsList.size()>=1){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
            bikeRideResponse.setData(rideEventsList);
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse getPrivateRide(String userId) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        List<RideEvent> rideEventsList=rideRepository.getPrivateRide(userId);
        if(rideEventsList.size()>=1){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
            bikeRideResponse.setData(rideEventsList);
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse joinRide(JoinRideRequest joinRideRequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        int ride=rideRepository.joinRide(joinRideRequest);
        if(ride==1){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse addUser(AddUserToRide addUserToRide) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        bikeRideResponse=checkForAdminUser(addUserToRide.getRideId(),addUserToRide.getUserId(),"ADD");
        if("2000".equalsIgnoreCase(bikeRideResponse.getResCode())){
            return bikeRideResponse;
        }
        int addUs=rideRepository.addUser(addUserToRide);
        if(addUs>0){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }

    private BikeRideResponse checkForAdminUser(String rideId, String userId, String command){
        log.info("RideServiceImpl checkForAdminUser rideId:{}, userId:{}",rideId,userId);
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        bikeRideResponse.setResCode("2000");
        bikeRideResponse.setResStatus("Success");
        RideRequest rideRequest=new RideRequest();
        rideRequest.setRideId(rideId);
        RideEvent rideEvent=rideRepository.rideDetails(rideRequest);
        if(rideEvent==null){
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setErrorDesc("Ride Not Found");
        }else if(rideEvent.getUserId()!=userId){
            bikeRideResponse.setResCode("4002");
            bikeRideResponse.setErrorDesc("Riders created the ride can only add or remove people!");
        }else if(rideEvent.getUser()!=null && rideEvent.getUser().size()==1 && "DELETE".equalsIgnoreCase(command)){
            bikeRideResponse.setResCode("4003");
            bikeRideResponse.setErrorDesc("Please delete the ride");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse removeUser(AddUserToRide addUserToRide) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        bikeRideResponse=checkForAdminUser(addUserToRide.getRideId(),addUserToRide.getUserId(),"DELETE");
        if("2000".equalsIgnoreCase(bikeRideResponse.getResCode())){
            return bikeRideResponse;
        }
        int addUs=rideRepository.removeUser(addUserToRide);
        if(addUs>0){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse rideDetails(RideRequest rideRequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        RideEvent rideEvent=rideRepository.rideDetails(rideRequest);
        if(rideEvent!=null){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
            bikeRideResponse.setData(rideEvent);
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse updateRide(RideRequest rideRequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        int row=rideRepository.updateRide(rideRequest);
        if(row!=0){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }

    @Override
    public BikeRideResponse deleteRide(RideRequest rideRequest) {
        BikeRideResponse bikeRideResponse=new BikeRideResponse();
        int row=rideRepository.deleteRide(rideRequest);
        if(row!=0){
            bikeRideResponse.setResCode("2000");
            bikeRideResponse.setResStatus("Success");
        }else{
            bikeRideResponse.setResCode("4001");
            bikeRideResponse.setResStatus("Failure");
        }
        return bikeRideResponse;
    }
}
