package com.bikeride.Controller;

import com.bikeride.Model.Response.BikeRideResponse;
import com.bikeride.Model.User;
import com.bikeride.Model.Request.Userrequest;
import com.bikeride.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> loginUser(@RequestBody Userrequest userrequest) {
        log.info("LoginController : loginUser{}",userrequest);
        User us=userService.getuser(userrequest);
        if(us!=null){
            return ResponseEntity.ok().body(us);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<BikeRideResponse> registerUser(@RequestBody Userrequest userrequest) {
        log.info("LoginController : registerUser{}",userrequest);
        BikeRideResponse bikeRideResponse=userService.registeruser(userrequest);
        return ResponseEntity.ok().body(bikeRideResponse);
    }

    @PostMapping("/forgot")
    public ResponseEntity<BikeRideResponse> forgotPasswordOrUsername(@RequestBody Userrequest userrequest) {
        log.info("LoginController : forgotPasswordOrUsername{}",userrequest);
        BikeRideResponse bikeRideResponse=userService.forgotPasswordOrUsername(userrequest);
        return ResponseEntity.ok().body(bikeRideResponse);
    }
}
