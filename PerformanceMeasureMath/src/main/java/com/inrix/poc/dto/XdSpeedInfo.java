package com.inrix.poc.dto;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
public class XdSpeedInfo {
    public double getSpeed() {
        return speed;
    }




    public double getHistoricAverageSpeed() {
        return historicAverageSpeed;
    }




    public double getReferenceSpeed() {
        return referenceSpeed;
    }




    public XdSpeedInfo(double speed, double historicAverageSpeed,
                       double referenceSpeed) {
        super();
        this.speed = speed;
        this.historicAverageSpeed = historicAverageSpeed;
        this.referenceSpeed = referenceSpeed;
    }




    final private double speed;

    final private double historicAverageSpeed;
    final private double referenceSpeed;

}
