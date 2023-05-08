package com.example.demo;


import java.io.Serializable;

/**
 * @author: yanghaoxian
 * @created: 2023/05/05
 * @description:
 */
public class Data implements Serializable {

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
