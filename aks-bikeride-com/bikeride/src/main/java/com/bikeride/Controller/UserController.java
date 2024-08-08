package com.bikeride.Controller;

import com.bikeride.Model.User;
import com.bikeride.Model.Userrequest;
import com.bikeride.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
   UserService userService;

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody Userrequest userrequest) {
       //userService.getuser()
        return ResponseEntity.ok().body("Success");
    }

   @GetMapping("/user")
   public ResponseEntity<User> fetchUser(@RequestBody Userrequest userrequest) {
      User user=userService.getuser(userrequest);
      return ResponseEntity.ok().body(user);
   }
}
