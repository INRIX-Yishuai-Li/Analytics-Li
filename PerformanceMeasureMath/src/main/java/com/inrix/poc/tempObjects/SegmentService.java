package com.inrix.poc.tempObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yishuai.Li on 6/17/2016.
 */
public class SegmentService {
    public void loadSegData(List<Integer> segIds){

    }

    public SegInfoList getAllSegInfo(){
        return new SegInfoList(1601, new ArrayList<SegInfo>());
    }

    public SegInfo getSegInfo(int segId){
        return new SegInfo();
    }

}
