package com.bikeride.Service;

import com.bikeride.Model.User;
import com.bikeride.Model.Userrequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    int createUser(Userrequest userrequest);
    User getuser(Userrequest userrequest);
}
