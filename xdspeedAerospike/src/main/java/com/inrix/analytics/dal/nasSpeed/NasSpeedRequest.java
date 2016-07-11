package com.inrix.analytics.dal.nasSpeed;

/**
 * Created by Yishuai.Li on 6/20/2016.
 */

import com.aerospike.client.*;
import com.aerospike.client.policy.Policy;
import com.amazonaws.services.s3.AmazonS3Client;
import com.inrix.analytics.dal.datasource.DALConfigurations;
import com.inrix.analytics.dal.datasource.AerospikeProperties;
import com.inrix.analytics.dal.interfaces.IDALConfiguration;
import com.inrix.analytics.dal.interfaces.IDALRequest;
import com.inrix.analytics.dal.interfaces.IDALResponse;
import com.inrix.analytics.logging.IRequestContext;
import com.inrix.common.binning.*;
import com.inrix.security.aws.IObjectStore;
import com.inrix.security.aws.S3ObjectStore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class NasSpeedRequest implements IDALRequest<SegNasSpeed>{
    public static final String CONFIG_NAME = "PerformanceMeasureAerospikeConfig";

    private IRequestContext requestContext = null;

    private List<SegInfo> segments;
    private final int granularity;
    private String nameSpaceName;
    private String mapVersion;
    private DateTime startTime;
    private DateTime endTime;
    private Binner binner;
    private int iterator;
    private AerospikeClient[] clients;

    public NasSpeedRequest(List<SegInfo> segments, int granularity, DateTime dtUtcStart, DateTime dtUtcEnd){
        this.segments = segments;
        this.granularity = granularity;
        this.mapVersion = "1601";
        this.startTime = dtUtcStart;
        this.endTime = dtUtcEnd;
        this.nameSpaceName = Constants.nasMap1601Namespace;
        binner = null;
        iterator = 0;
    }

    public NasSpeedRequest(List<SegInfo> segments, int granularity, String mapVersion, DateTime dtUtc, DateTime dtUtcEnd){
        this.segments = segments;
        this.granularity = granularity;
        this.mapVersion = mapVersion;
        this.startTime = dtUtc;
        this.endTime = dtUtcEnd;
        if (mapVersion.equals("1302")) {
            this.nameSpaceName = Constants.nasMap1302Namespace;
        } else if (mapVersion.equals("1303")) {
            this.nameSpaceName = Constants.nasMap1303Namespace;
        } else if (mapVersion.equals("1403")) {
            this.nameSpaceName = Constants.nasMap1403Namespace;
        } else if (mapVersion.equals("1501")) {
            this.nameSpaceName = Constants.nasMap1501Namespace;
        } else if (mapVersion.equals("1502")) {
            this.nameSpaceName = Constants.nasMap1502Namespace;
        } else if (mapVersion.equals("1601")) {
            this.nameSpaceName = Constants.nasMap1601Namespace;
        } else {
            this.nameSpaceName = "not";
        }
        binner = null;
        iterator = 0;

    }

    public NasSpeedRequest withRequestContext(final IRequestContext requestContext) {
        this.requestContext = requestContext;
        return this;
    }

   //@Override
   IDALResponse<SegNasSpeed> execute(AerospikeProperties asProperties){

        try {
            clients = asProperties.getConfig();

            getBinner();

            List<SegNasSpeed> nasSpeeds = new ArrayList<SegNasSpeed>();
            List<String> errors = new ArrayList<String>();

            for(SegInfo seg : segments){
                nasSpeeds.add(getNasSpeedPerSeg(seg));
            }

            return new IDALResponse<SegNasSpeed>(nasSpeeds.toArray(new SegNasSpeed[0]), errors.toArray(new String[0]));
        } catch (AerospikeException ex) {
            return new IDALResponse<SegNasSpeed>(ex);
        } catch (IOException ex) {
            return new IDALResponse<SegNasSpeed>(ex);
        } catch (Exception ex) { // AerospikeProperties error
            return new IDALResponse<SegNasSpeed>(ex);
        }
    }

    @Override
    public IDALResponse<SegNasSpeed> execute(){
        try {
            IDALConfiguration dalConfig = DALConfigurations.get(NasSpeedRequest.CONFIG_NAME);
            if (dalConfig == null) {
                String message = String.format("%1$s not set in configuration", NasSpeedRequest.CONFIG_NAME);
                throw new RuntimeException(message);
            }
            AerospikeProperties props = AerospikeProperties.get(dalConfig);
            return this.execute(props);
        }catch (Exception ex) {
            return new IDALResponse<SegNasSpeed>(ex);
        }
    }

    private void getBinner() throws IOException {
        if(binner == null){
            AmazonS3Client s3client = new AmazonS3Client();
            try {
                IObjectStore store = new S3ObjectStore(s3client);
                binner = BinnerFactory.createInstance("cseg", store, "inrixprod-referencedata", mapVersion);
            }catch(Exception e){
                throw new IOException("Failed to get binner, check S3 for region and holiday reference data");
            }
        }
    }

    private SegNasSpeed getNasSpeedPerSeg (SegInfo segment){
        iterator = iterator++ % clients.length;
        int[] xdspeed  = new int[960];
        for (int i = 0; i < 960; i++) {
            xdspeed[i] = -1;
        }
        Policy policy = new Policy();
        policy.timeout = 30000;
        ByteBuffer key = ByteBuffer.allocate(4);
        key.putInt(segment.getXdSegId());
        key.rewind();
        String a = null;
        Key xdkey = new Key(nameSpaceName, a, key.array());
        Record record = clients[iterator].get(policy, xdkey);

        if (record != null) {
            String nas = (String) record.getValue("s");
            // System.out.println("kaab,"+nas);
            String[] tokens = nas.split(",");
            for (int i = 0; i < tokens.length; i++) {
                String keyval[] = tokens[i].split(":");
                xdspeed[Integer.parseInt(keyval[0])] = Integer
                        .parseInt(keyval[1]);

            }
        }

        ArrayList<Integer> nasSpeed = new ArrayList<Integer>();
        while(startTime.compareTo(endTime) <=0){
            nasSpeed.add(xdspeed[binner.getBin(startTime, segment.getPgId()).BinId]);
            startTime = startTime.plusMinutes(granularity);
        }

        return new SegNasSpeed(granularity, nasSpeed);
    }

    public IRequestContext getRequestContext() {
        return requestContext;
    }

}
