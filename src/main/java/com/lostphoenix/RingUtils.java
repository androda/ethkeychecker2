package com.lostphoenix;

public class RingUtils {


    public static class Segment {
        Color color;
        Thickness thickness;

        public Segment(Color color, Thickness thickness) {
            this.color = color;
            this.thickness = thickness;
        }
    }

    public static Segment[] segmentProperties = new Segment[]{
            new Segment(Color.DG, Thickness.S),
            new Segment(Color.LG, Thickness.S),
            new Segment(Color.PINK, Thickness.S),
            new Segment(Color.BLUE, Thickness.S),
            new Segment(Color.DG, Thickness.M),
            new Segment(Color.LG, Thickness.M),
            new Segment(Color.PINK, Thickness.M),
            new Segment(Color.BLUE, Thickness.M),
            new Segment(Color.DG, Thickness.L),
            new Segment(Color.LG, Thickness.L),
            new Segment(Color.PINK, Thickness.L),
            new Segment(Color.BLUE, Thickness.L),
            new Segment(Color.DG, Thickness.XL),
            new Segment(Color.LG, Thickness.XL),
            new Segment(Color.PINK, Thickness.XL),
            new Segment(Color.BLUE, Thickness.XL),

    };
}
