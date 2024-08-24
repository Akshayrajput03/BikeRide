package com.bikeride.Model;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String mobileNum;
}
