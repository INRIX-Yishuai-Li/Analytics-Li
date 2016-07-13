package com.inrix.poc.tempObjects;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
public class SegInfo {
    int xdSegId;
    int pgId;
    double lengthInMiles;
    double refSpeedInMph;

    public int getXdSegId() {
        return xdSegId;
    }

    public void setXdSegId(int xdSegId) {
        this.xdSegId = xdSegId;
    }

    public int getPgId() {
        return pgId;
    }

    public void setPgId(int pgId) {
        this.pgId = pgId;
    }

    public double getLengthInMiles() {
        return lengthInMiles;
    }

    public void setLengthInMiles(double lengthInMiles) {
        this.lengthInMiles = lengthInMiles;
    }

    public double getRefSpeedInMph() {
        return refSpeedInMph;
    }

    public void setRefSpeedInMph(double refSpeedInMph) {
        this.refSpeedInMph = refSpeedInMph;
    }
}
