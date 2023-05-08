package com.inspur.zdp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class EndpointNameEntity implements Serializable {
    public String name;
    public Map<String, String> tags;
    public Long time;
    public ArrayList<EndpointEntity> points;

    public EndpointNameEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<EndpointEntity> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<EndpointEntity> points) {
        this.points = points;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
