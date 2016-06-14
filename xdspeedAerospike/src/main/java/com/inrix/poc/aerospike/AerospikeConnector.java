package com.inrix.poc.aerospike;

/**
 * Created by Yishuai.Li on 6/13/2016.
 */

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;

import java.nio.ByteBuffer;
import java.util.Date;


public class AerospikeConnector {
    int xdspeed[] = null;
    Policy policy;
    ByteBuffer key;
    private int iterator;
    AerospikeClient[] clients = null;
    String nameSpaceName;
    String mapVersion;
    private Binner binner;

    private String[] hosts;

    public AerospikeConnector(){
        this.nameSpaceName = Constants.nasMap1601Namespace;
        hosts = Utils.getAeroSpikeHosts();
        mapVersion = "1601";
        getReadyToLoad();
    }

    public AerospikeConnector(String mapVersionIn){
        mapVersion = mapVersionIn;
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

        hosts = Utils.getAeroSpikeHosts();
        getReadyToLoad();
    }
    
    public void setHosts(String[] hosts){
        this.hosts = hosts;
    }

    public void getReadyToLoad(){
        clients = new AerospikeClient[hosts.length];

        iterator = 0;
        if (xdspeed == null)
            xdspeed = new int[960];
        for (int i = 0; i < 960; i++) {
            xdspeed[i] = -1;
        }
        for (int i = 0; i < hosts.length; i++) {
            // /System.out.println(i+"....."+hosts[i]);
            clients[i] = new AerospikeClient(hosts[i], 3000);
        }
        policy = new Policy();
        policy.timeout = 30000;
        key = ByteBuffer.allocate(8);
        binner = new Binner(mapVersion);
    }

    public boolean loadLocalHashMap(int xd) throws AerospikeException {

        iterator = iterator++ % clients.length;

        if (xdspeed == null) {
            xdspeed = new int[960];
        }
        // xdspeed.clear();
        for (int i = 0; i < 960; i++) {
            xdspeed[i] = -1;
        }
        Policy policy = new Policy();
        policy.timeout = 30000;
        ByteBuffer key = ByteBuffer.allocate(4);
        key.putInt(xd);
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
                // xdspeed.put(xd+""+keyval[0],
                // Bytes.toBytes(Integer.parseInt(keyval[1])));
                xdspeed[Integer.parseInt(keyval[0])] = Integer
                        .parseInt(keyval[1]);

            }
            return true;
        }
        return false;
    }

    public void close() {
        for (int i = 0; i < clients.length; i++) {
            if (clients[i] != null)
                clients[i].close();
        }

    }

    public int speed(int dtk, int xd, int pgid) {
        return xdspeed[binner.getBinId(dtk, pgid)];
    }


    /**
     *   Sample call : speed(currentTime + iterator * 60000, xd, pgid)
     *   currentTime: is the start timestamp of that day
     *   iterator: is the minute of that day
     */
    public int speed(long ts, int xd, int pgid) {
        return xdspeed[binner.getBinId(getDtk(ts), pgid)];
    }

    static int getDtk(long ts) {
        Date date1 = new Date(978307200000L);
        Date date2 = new Date(ts);
        long difference = date2.getTime() - date1.getTime();
        return (int) ((difference / 1000) / 60);
    }

}
