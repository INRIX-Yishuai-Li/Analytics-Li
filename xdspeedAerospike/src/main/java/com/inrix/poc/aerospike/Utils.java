package com.inrix.poc.aerospike;


/**
 * Created by Yishuai.Li on 6/14/2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static String[] getAeroSpikeHosts()

    //
    {
        String[] hosts = {"54.186.30.60","54.186.43.46","54.186.97.249","54.201.89.95","54.149.231.142","54.201.50.213"};
        return hosts;

    }

    public static long convertToTimeInMilliesWithTimeInDefaultTimeZone(
            String dateTime, String format) {
        try {
            // SimpleDateFormat dateFormatGmt =
            // getSimpleDateFormatInstanceWithDefaultTimeZone(format);
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format);
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone(Constants.timeZone));
            Date date = dateFormatGmt.parse(dateTime);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMapVersion (long myUtc)
    {
        String mapVersion = null;
        // Map 1302: 1-12-2013 to 31-12-2012
        if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2013:12:01 00:00", "yyyy:MM:dd HH:mm")
                && myUtc <= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2013:12:21 23:59",
                        "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1302Namespace;
        }
        // Map 1303: 1-1-2014 to 20-10-2014
        if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2014:01:01 00:00", "yyyy:MM:dd HH:mm")
                && myUtc <= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2014:10:20 23:59",
                        "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1303Namespace;

        }
        // Map 1403: 21-10-2014 to 28-4-2015
        else if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2014:10:21 00:00", "yyyy:MM:dd HH:mm")
                && myUtc <= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2015:04:28 23:59",
                        "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1403Namespace;

        }
        // Map 1501: 29-4-2015 to 1-10-2015
        else if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2015:04:29 00:00", "yyyy:MM:dd HH:mm")
                && myUtc <= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2015:10:01 23:59",
                        "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1501Namespace;

        }
        // Map 1502: 2-10-2015 to current
        else if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2015:10:2 00:00", "yyyy:MM:dd HH:mm")
                && myUtc <= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2016:03:28 23:59",
                        "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1502Namespace;

        }

        else if (myUtc >= Utils
                .convertToTimeInMilliesWithTimeInDefaultTimeZone(
                        "2016:03:29 00:00", "yyyy:MM:dd HH:mm")) {
            mapVersion = Constants.nasMap1601Namespace;

        }
        return mapVersion;
    }

    public static long dtkToMillis(int dateTimeKey) {
        long startMillis = 978307200000L;

        return startMillis + (long)dateTimeKey * 60000;
    }
}
