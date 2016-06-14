package com.inrix.poc.aerospike;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
import java.util.Map;

import org.joda.time.DateTime;
import com.inrix.poc.aerospike.Regions.RegionInfo;

public class Binner {
    public Binner(String mapVersion) {
        super();
		/*this.mapVersion = mapVersion;*/
        this.regionsInfo=new Regions(mapVersion);
        this.holidayInfo=new HolidayInfo();
    }
    /*private final String mapVersion;*/
    private Regions regionsInfo;
    private HolidayInfo holidayInfo;
    public int getBinId(int dtk,int pgid){
        DateTime localDt = regionsInfo.getLocalDateOfRegion(pgid, new DateTime(Utils.dtkToMillis(dtk)));
        RegionInfo ri = regionsInfo.getRegionInfo(pgid);
        Map<String, Integer> holidays=holidayInfo.getHolidayInfo(ri.holidayRegionId);
        int holidayTypeId;
        if (holidays == null) {
            holidayTypeId = 0;
        } else {
            String key = String.format("%04d-%02d-%02d",localDt.getYear(),localDt.getMonthOfYear(),localDt.getDayOfMonth());
            Integer ht = holidays.get(key);
            //System.out.println(key+ht);
            if (ht == null) {
                holidayTypeId = 0;
            } else {
                holidayTypeId = ht;
            }
        }

        return   getBinIdLocal(localDt, holidayTypeId);
    }
    private int getBinIdLocal(DateTime localDt, int holidayTypeId) {
        int dayOfWeek;
        if (holidayTypeId == 0) {
            // non-holiday
            dayOfWeek = localDt.getDayOfWeek();
            if (dayOfWeek == 7) {
                dayOfWeek = 0;
            }
        } else {
            // holiday
            dayOfWeek = 6 + holidayTypeId;
        }
        return dayOfWeek * 96 + localDt.getMillisOfDay() / (15 * 60 * 1000);
    }

    public static void main (String args[]){
        Regions ri=new Regions("nas1302");
        ri.getLocalDateOfRegion(5, new DateTime());


    }
}
