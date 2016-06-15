package com.inrix.poc.aerospike;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Yishuai.Li on 6/13/2016.
 */
public class testRun {
    public static void main(String[] args) throws IOException, ParseException {
        /*
        String[] hosts = {"54.186.30.60","54.186.43.46","54.186.97.249","54.201.89.95","54.149.231.142","54.201.50.213"};
        AerospikeSample connector = new AerospikeSample(hosts);
        connector.loadLocalHashMap(293505);

        System.out.println(connector.xdspeed.length);
        for(int i = 0; i< connector.xdspeed.length;i++){
            System.out.println(connector.xdspeed[i]);
        }
        */
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        String startDate = "2016-06-10 16:00";


        //"2016-06-10 16:59"   1465516800000
        //"2016-06-10 17:00"   1465603200000

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");

        calendar.setTime(dateFormat.parse(startDate));
        System.out.println(calendar.getTimeInMillis()
                - calendar.getTimeInMillis()
                % 86400000);
        System.out.println(calendar.getTimeInMillis());

        int iterator = 0;
        iterator = iterator++ % 4;
        System.out.println(iterator);


        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(3559702);
        buffer.putLong(1465925897);
        buffer.rewind();
        System.out.println(buffer.toString());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());

        AerospikeConnector ac = new AerospikeConnector();
        ac.loadLocalHashMap(3559702);
        System.out.println(ac.xdspeed[309]);

        for(int i = 0; i < 20 ; i++){
            System.out.println(ac.xdspeed[i]);
        }

        for(int i = 0; i< 10; i+=3){
            System.out.println(i);
        }

    }
}
