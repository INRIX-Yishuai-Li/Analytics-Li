package com.inrix.poc.performanceMeasure;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
public class Metrics {
    double speed;
    double travelTime;
    double travelTimeHistoric;

    public Metrics(){

    }

    public Metrics(double speed, double travelTime){
        this.speed = speed;
        this.travelTime = travelTime;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }



}
