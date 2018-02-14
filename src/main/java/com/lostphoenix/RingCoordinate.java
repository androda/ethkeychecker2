package com.lostphoenix;

public class RingCoordinate {
    public final int ring;
    public final int slice;

    public RingCoordinate(int ring, int slice) {
        this.ring = ring;
        this.slice = slice;
    }


    public int getSegmentIndex(){
        return (((16-ring)/4)+(slice/4))%4;
    }
//    public int getSegmentInRing(){
//        return
//    }


}
