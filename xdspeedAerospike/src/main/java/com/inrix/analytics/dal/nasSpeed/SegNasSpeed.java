package com.inrix.analytics.dal.nasSpeed;

import java.util.List;

/**
 * Created by Yishuai.Li on 6/21/2016.
 */
public class SegNasSpeed {
    private final int granularity;
    private final List<Integer> nasSpeeds;

    public int getGranularity() {
        return granularity;
    }

    public List<Integer> getNasSpeeds() {
        return nasSpeeds;
    }

    public SegNasSpeed(int granularity, List<Integer> nasSpeeds){
        this.granularity = granularity;
        this.nasSpeeds = nasSpeeds;
    }
}
