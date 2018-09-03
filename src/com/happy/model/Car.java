package com.happy.model;

import java.io.Serializable;

/**
 * @author kyle
 * @version 1.0
 * @className
 * @descripsion TODO
 * @date 2018/8/28 13:36
 */
public class Car implements Serializable {
    private Long id;
    private String brand;
    private String country;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
