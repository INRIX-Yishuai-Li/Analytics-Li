package com.inrix.poc.performanceMeasure;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */

import com.inrix.analytics.dal.speedarchive.*;
import com.inrix.poc.aerospike.*;
import com.inrix.poc.aerospike.AerospikeConnector;
import com.inrix.poc.aerospike.Regions;
import com.inrix.poc.dto.XdSpeedInfo;
import com.inrix.poc.dto.XdSpeedList;
import com.inrix.poc.tempObjects.*;
//import com.inrix.poc.tempObjects.Speed;
import com.inrix.analytics.dal.speedarchive.Speed;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.inrix.analytics.dal.interfaces.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class math {
    AerospikeConnector ac ;
    //SoeSpeedService soeService ;
    SpeedArchiveRequest soeService ;

    public void metrixEngine(String startDate, String endDate, List<Integer> segIdList, int granularity) throws Exception {
        //soeService = new SoeSpeedService();
        ac = new AerospikeConnector();

        // !!! For now set map version as 1601, change to dynamic accordingly
        Regions regionsInfo = new Regions("1601");

        DateTime startTime = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC));
        DateTime endTime = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC));

        SegInfoList allSegs = new SegmentService().getAllSegInfo();
        List<SegInfo> allSegsList = new ArrayList<SegInfo>();
        allSegsList = allSegs.getSegInfoList();

        List<List<Metrics>> allDayMetrics = new ArrayList<List<Metrics>>();
        while(startTime.compareTo(endTime) <= 0){
            List<List<Speed>> corridorSpeed = new ArrayList<List<Speed>>();
            for(int i = 0; i< allSegsList.size(); i++){
                DateTime start = getUTCtimeFromLocal(regionsInfo, allSegsList.get(i).getPgId(), DateTimeFormat.forPattern("yyyy-MM-dd").print(startTime));
                DateTime end = start.plusDays(1);
                List<Speed> speedPerSeg = new ArrayList<Speed>();

                // How to deal with hbase new return
                //soeService = new SpeedArchiveRequest(allSegsList.get(i), start, end, granularity, null);
                IDALResponse<Speed> res = soeService.execute();

                // hbase call end


                corridorSpeed.add(speedPerSeg);
            }

            List<Metrics> dailyMetrics = new ArrayList<Metrics>();
            try {
                dailyMetrics = getDailyMetrics(startTime.getMillis(), allSegs, corridorSpeed, granularity);
            } catch (Exception e) {
                throw new Exception("Failed get metrics for date:" + DateTimeFormat.forPattern("yyyy-MM-dd").print(startTime) + " Error detail:" + e.getMessage());
            }
            allDayMetrics.add(dailyMetrics);
            startTime = startTime.plusDays(1);
        }

        //SpeedArchiveRequest req = new SpeedArchiveRequest(xdSegIds, from, to, 1, null);

    }


    public List<Metrics> getDailyMetrics(long current, SegInfoList seglist, List<List<Speed>> soeSpeedAllSegs, int granularity) throws Exception {
        //ac = new AerospikeConnector();

        List<XdSpeedList> allSegSpeedData = new ArrayList<XdSpeedList>();
        List<SegInfo> segs = seglist.getSegInfoList();
        double corridorLength = 0;

        for(int i = 0; i < seglist.getSegSize(); i++ ){
            corridorLength += seglist.getSegInfoList().get(i).getLengthInMiles();
            try {
                allSegSpeedData.add(collectUnitSpeed(current, segs.get(i), soeSpeedAllSegs.get(i), ac, granularity));
            } catch (Exception e) {
                throw new Exception("SegId:" + segs.get(i).getXdSegId() + " " + e.getMessage());
            }
        }

        List<Metrics> metricsList = new ArrayList<Metrics>();
        for(int iterator = 0; iterator < (1440 / granularity); iterator++){
            double travelTime = 0;
            double travelTimeHistoric = 0;
            double travelTimeFreeFlow = 0;
            for(int i = 0; i < allSegSpeedData.size(); i++){
                XdSpeedList perSegSpeed = allSegSpeedData.get(i);
                travelTime += (perSegSpeed.getSegmentLength() / perSegSpeed.getSpeed(iterator));
                travelTimeHistoric += (perSegSpeed.getSegmentLength() / perSegSpeed.getHistoricSpeed(iterator));
                travelTimeFreeFlow += (perSegSpeed.getSegmentLength() / perSegSpeed.getRefSpeed(iterator));
            }
            double unitSpeed = roundTo2DecPlaces(corridorLength / travelTime) ;
            double unitHistoricSpeed = roundTo2DecPlaces(corridorLength / travelTimeHistoric) ;
            double unitFreeFlowSpeed = roundTo2DecPlaces(corridorLength / travelTimeFreeFlow) ;
            double unitTravelTimeIndex = roundTo2DecPlaces(travelTime / travelTimeFreeFlow) ;
            double unitCongestion = roundTo2DecPlaces(travelTimeFreeFlow / travelTime) ;

            Metrics unitMetrics = new Metrics();

            metricsList.add(unitMetrics);
        }

        return metricsList;
    }

    private XdSpeedList collectUnitSpeed(long current, SegInfo seg, List<Speed> soeSpeed, AerospikeConnector ac, int granularity) throws Exception {
        if(soeSpeed.size()!= (1440 / granularity)) {
            throw new Exception("SoeSpeed list size does NOT match granularity, list size:" + soeSpeed.size() + " granularity:" + granularity);
        }
        XdSpeedList speedPerSeg = new XdSpeedList(seg.getXdSegId(), seg.getLengthInMiles());
        ac.loadLocalHashMap(seg.getXdSegId());
        for (int iterator = 0; iterator < (1440 / granularity); iterator++ ){
            XdSpeedInfo unit ;
            Speed unitSoe = soeSpeed.get(iterator);
            double unitNasSpeed = ac.speed(current + iterator * granularity * 60000,  seg.getPgId());

            // For now, if score is 'a' or 'b', soe use hbase speed, nas use aerospike, reference use ref from segment. Only when score is 'c' then
            // we use ref speed for all three
            if(unitSoe.getScore() == 'c'){
                unit = new XdSpeedInfo(unitSoe.getSpeed(), unitNasSpeed * 1.609344, seg.getRefSpeedInMph());
            }else{
                unit = new XdSpeedInfo(seg.getRefSpeedInMph(), seg.getRefSpeedInMph(), seg.getRefSpeedInMph());
            }

            speedPerSeg.add(iterator, unit);
        }

        return speedPerSeg;
    }

    public static double getPercentile(){
        return -1;
    }

    public static double roundTo2DecPlaces(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public DateTime getUTCtimeFromLocal(Regions regionsInfo, int regionId, String queryTime){
        return DateTime.parse(queryTime, DateTimeFormat.forPattern("yyyy-MM-dd").withZone(regionsInfo.getRegionInfo(regionId).Timezone));
    }

}
