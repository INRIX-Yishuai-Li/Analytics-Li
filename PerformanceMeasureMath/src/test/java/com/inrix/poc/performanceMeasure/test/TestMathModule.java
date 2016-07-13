package com.inrix.poc.performanceMeasure.test;

/**
 * Created by Yishuai.Li on 6/15/2016.
 */

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.inrix.poc.tempObjects.SegInfo;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestMathModule {

    @Test
    public void testCollectUnitSpeed(){

    }

    @Test
    public void testGetTravelTime() throws ParseException {
        //long testCurrent = 1464739200; // 6/1/2016 00:00
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String testDate = "2016-06-01 00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(testDate));
        long testCurrent = calendar.getTimeInMillis();

        SegInfo seg1 = new SegInfo();
        seg1.setXdSegId(3557545);
        seg1.setPgId(7);
        seg1.setLengthInMiles(0.65 / 1.609344);
        seg1.setRefSpeedInMph(93.0 / 1.609344);

        SegInfo seg2 = new SegInfo();
        seg1.setXdSegId(80763393);
        seg1.setPgId(1);
        seg1.setLengthInMiles(0.16 / 1.609344);
        seg1.setRefSpeedInMph(85.0 / 1.609344);



    }

    //getTravelTime(long current, SegInfoList seglist, List<List<Speed>> soeSpeedAllSegs, int granularity)
}
