package com.bikeride.Model.Response;

import lombok.Data;

@Data
public class BikeRideResponse {

    private String resCode;
    private String resStatus;
    private String errorDesc;
    private Object data;
}
