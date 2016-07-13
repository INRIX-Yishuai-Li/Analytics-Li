package com.inrix.poc.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
public class XdSpeedList implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Map<Integer, XdSpeedInfo> map;
    final double segmentLength;
    final int segmentId;

    public XdSpeedList(int segId,double segLength) {
        this.segmentId=segId;
        this.segmentLength=segLength;
        map = new HashMap<Integer, XdSpeedInfo>();
    }

    public Map<Integer, XdSpeedInfo> getMap() {
        return map;
    }

    public void setMap(Map<Integer, XdSpeedInfo> map) {
        this.map = map;
    }


    public void add(String col, XdSpeedInfo val){
        map.put(Integer.parseInt(col), val);
    }

    public void add(int col, XdSpeedInfo val){
        map.put(col, val);
    }



    public double getSegmentLength() {
        return segmentLength;
    }

    public int getSegmentId() {
        return segmentId;
    }


    public double getWeightedSpeed(int col){
        return segmentLength * (double) map.get(col).getSpeed();
    }
    public double getSpeed(int col){
        return (double) map.get(col).getSpeed();
    }
    public double getHistoricSpeed(int col){
        return (double) map.get(col).getHistoricAverageSpeed();
    }
    public double getRefSpeed(int col){
        return (double) map.get(col).getReferenceSpeed();
    }
    public double getWeightedHistoricSpeed(int col){
        return segmentLength * (double) map.get(col).getHistoricAverageSpeed();
    }
    public double getWeightedRefSpeed(int col){
        return segmentLength * (double) map.get(col).getReferenceSpeed();
    }

}
