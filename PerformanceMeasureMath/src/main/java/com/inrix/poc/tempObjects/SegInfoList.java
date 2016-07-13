package com.inrix.poc.tempObjects;

import java.util.List;

/**
 * Created by Yishuai.Li on 6/14/2016.
 */
public class SegInfoList {
    int mapVer;
    List<SegInfo> segs;

    public SegInfoList(int mapVer, List<SegInfo> segs){
        this.mapVer = mapVer;
        this.segs = segs;
    }

    public int getMapVersion(){
        return mapVer;
    }

    public List<SegInfo> getSegInfoList(){
        return segs;
    }

    public int getSegSize(){
        return segs.size();
    }
}
