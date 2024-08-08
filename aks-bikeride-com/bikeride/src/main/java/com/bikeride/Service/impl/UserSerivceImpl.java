package com.bikeride.Service.impl;

import com.bikeride.Model.User;
import com.bikeride.Model.Userrequest;
import com.bikeride.Repository.UserRepository;
import com.bikeride.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserSerivceImpl implements UserService {

    private final UserRepository userRepository;

    public UserSerivceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int createUser(Userrequest userrequest) {
        return 0;
    }

    @Override
    public User getuser(Userrequest userrequest) {
        return userRepository.getUser(userrequest);
    }
}
