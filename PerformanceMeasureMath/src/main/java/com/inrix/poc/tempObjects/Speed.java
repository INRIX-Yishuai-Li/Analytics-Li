package com.inrix.poc.tempObjects;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
import java.util.ArrayList;
import java.util.List;

public class Speed {
    private final double speed;
    private final double confidence;
    private final int score;
    private final String detail;

    public double getSpeed() {
        return speed;
    }

    public double getConfidence() {
        return confidence;
    }

    public int getScore() {
        return score;
    }

    public String getDetail() {
        return detail;
    }

    public Speed(double speed, double confidence, int score, String detail) {
        this.speed = speed;
        this.confidence = confidence;
        this.score = score;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return String.format("<Speed (speed=%f, confidence=%f, score=%d, detail=%s)>", speed, confidence, score, detail);
    }

    /*
    private static int toScore(String val) {
        int score;
        switch (val) {
            case "a":
                score = 30;
                break;
            case "b":
                score = 20;
                break;
            case "c":
                score = 10;
                break;
            default:
                score = -1;
        }
        return score;
    }

    public static List<Speed> deSerializeHBaseRow(String row) {
        String [] flds = row.split(",");
        List<Speed> speeds = new ArrayList<>();
        for (int i = 0; i < flds.length; i += 2) {
            double speed = Double.parseDouble(flds[i]);
            double confidence = -1;
            int score;
            String detail = "";

            String meta = flds[i + 1];
            if (meta.contains(":")) {
                // score:conf:detail
                String[] metaFlds = meta.split(":");
                score = toScore(metaFlds[0]);
                if (! metaFlds[1].isEmpty()) {
                    confidence = Double.parseDouble(metaFlds[1]);
                } else if (! metaFlds[2].isEmpty()) {
                    detail = metaFlds[2];
                }
            } else {
                score = toScore(meta);
            }
            speeds.add(new Speed(speed, confidence, score, detail));
        }
        return speeds;
    }
    */
}
