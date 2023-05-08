package com.inspur.zdp.util;

import java.io.Serializable;
import java.util.ArrayList;

public class EndpointReadEntity implements Serializable {


    private ArrayList<EndpointEntity>  values;

    public EndpointReadEntity() {
    }

    public ArrayList<EndpointEntity> getValues() {
        return values;
    }

    public void setValues(ArrayList<EndpointEntity> values) {
        this.values = values;
    }
}
