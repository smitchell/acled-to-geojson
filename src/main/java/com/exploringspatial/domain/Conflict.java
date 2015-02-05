/*
 * Copyright (c) 2015 Exploring Spatial. The MIT License (MIT)
 */

package com.exploringspatial.domain;

import java.util.Date;

/**
 * Created by Steve on 1/17/15.
 */
public class Conflict {
    private Integer gwno;
    private String eventIdCountry;
    private Long eventPk;
    private Date eventDate;
    private Integer year;
    private Integer timePrecision;
    private String eventType;
    private String actor1;
    private String allyActor1;
    private Integer inter1;
    private String actor2;
    private String allyActor2;
    private Integer inter2;
    private Integer interaction;
    private String country;
    private String admin1;
    private String admin2;
    private String admin3;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer geoPrecision;
    private String source;
    private String notes;
    private Integer fatalities;
    private String path;

    public Integer getGwno() {
        return gwno;
    }

    public void setGwno(Integer gwno) {
        this.gwno = gwno;
    }

    public String getEventIdCountry() {
        return eventIdCountry;
    }

    public void setEventIdCountry(String eventIdCountry) {
        this.eventIdCountry = eventIdCountry;
    }

    public Long getEventPk() {
        return eventPk;
    }

    public void setEventPk(Long eventPk) {
        this.eventPk = eventPk;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTimePrecision() {
        return timePrecision;
    }

    public void setTimePrecision(Integer timePrecision) {
        this.timePrecision = timePrecision;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getAllyActor1() {
        return allyActor1;
    }

    public void setAllyActor1(String allyActor1) {
        this.allyActor1 = allyActor1;
    }

    public Integer getInter1() {
        return inter1;
    }

    public void setInter1(Integer inter1) {
        this.inter1 = inter1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public String getAllyActor2() {
        return allyActor2;
    }

    public void setAllyActor2(String allyActor2) {
        this.allyActor2 = allyActor2;
    }

    public Integer getInter2() {
        return inter2;
    }

    public void setInter2(Integer inter2) {
        this.inter2 = inter2;
    }

    public Integer getInteraction() {
        return interaction;
    }

    public void setInteraction(Integer interaction) {
        this.interaction = interaction;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdmin1() {
        return admin1;
    }

    public void setAdmin1(String admin1) {
        this.admin1 = admin1;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
    }

    public String getAdmin3() {
        return admin3;
    }

    public void setAdmin3(String admin3) {
        this.admin3 = admin3;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getGeoPrecision() {
        return geoPrecision;
    }

    public void setGeoPrecision(Integer geoPrecision) {
        this.geoPrecision = geoPrecision;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getFatalities() {
        return fatalities;
    }

    public void setFatalities(Integer fatalities) {
        this.fatalities = fatalities;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
