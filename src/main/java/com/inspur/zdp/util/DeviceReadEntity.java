package com.inspur.zdp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceReadEntity implements Serializable {

    public ArrayList<HashMap<String,EndpointEntity>> points;

    public DeviceReadEntity() {
    }

    public ArrayList<HashMap<String,EndpointEntity>> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<HashMap<String,EndpointEntity>> points) {
        this.points = points;
    }
}
