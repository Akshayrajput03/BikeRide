package com.bikeride.Controller;

import com.bikeride.Model.User;
import com.bikeride.Model.Userrequest;
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
        log.info("LoginController :{}",userrequest);
        User us=userService.getuser(userrequest);
        if(us!=null){
            return ResponseEntity.ok().body(us);
        }
        return ResponseEntity.notFound().build();
    }
}
