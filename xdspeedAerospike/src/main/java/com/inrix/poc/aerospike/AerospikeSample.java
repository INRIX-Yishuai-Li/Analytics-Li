package com.inrix.poc.aerospike;

/**
 * Created by Yishuai.Li on 6/13/2016.
 */

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AerospikeSample {
    static String[] hosts = {"54.186.30.60","54.186.43.46","54.186.97.249","54.201.89.95","54.149.231.142","54.201.50.213"};

    int xdspeed[] = null;
    Policy policy;
    ByteBuffer key;
    private int iterator;
    AerospikeClient[] clients = null;
    final String nameSpaceName;

    public AerospikeSample(String[] hosts)
            throws IOException, AerospikeException {

        this.nameSpaceName = "nas1601";

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

    public static void main(String args[]) throws AerospikeException,
            IOException {
        AerospikeSample connector = new AerospikeSample(hosts);
        connector.loadLocalHashMap(293505);


    }


}
