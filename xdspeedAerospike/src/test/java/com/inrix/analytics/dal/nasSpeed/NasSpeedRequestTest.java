package com.inrix.analytics.dal.nasSpeed;

import com.inrix.analytics.dal.datasource.DALConfigurations;
import com.inrix.analytics.dal.interfaces.IDALConfiguration;
import com.inrix.analytics.dal.interfaces.IDALResponse;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yishuai.Li on 6/21/2016.
 */
public class NasSpeedRequestTest {

    @Before
    public void before() {
        IDALConfiguration dalConfiguration = new DALConfigurationTest();
        DALConfigurations.add(dalConfiguration);
    }

    @Test
    public void testExecute() {
        DateTime start = DateTime.parse("2016-06-01", DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC));
        DateTime end = start.plusDays(1);
        SegInfo segment = new SegInfo(3557545, 7, 0.65 / 1.609344, 93.0 / 1.609344);
        List<SegInfo> seglist = new ArrayList<SegInfo>();
        seglist.add(segment);

        NasSpeedRequest request = new NasSpeedRequest(seglist, 60, "1601", start, end);
        DALConfigurationTest config = new DALConfigurationTest();
        IDALResponse<SegNasSpeed> res = request.execute();
        if (res.getException() != null) {
            // unable to get any results at all.
        }

        if (res.getErrors() !=null & res.getErrors().length > 0) {
            // res still has results but with caveats
        }

        System.out.println("length!!!" + res.getObjects().length);

        for (SegNasSpeed speed : res.getObjects()) {
            List<Integer> nasSpeeds = speed.getNasSpeeds();
            assertEquals(nasSpeeds.size(), 25);
            for(int i = 0; i< nasSpeeds.size() ; i++){
                System.out.println(nasSpeeds.get(i));
            }
        }
    }

    private class DALConfigurationTest implements IDALConfiguration {
        @Override
        public String getName() {
            return "PerformanceMeasureAerospikeConfig";
        }

        @Override
        public String getS3Bucket() {
            return "analytics-application-configuration";
        }

        @Override
        public String getS3Key() {
            return "analytics.performance-measure.aerospike.config.prod.properties";
        }
    }
}
