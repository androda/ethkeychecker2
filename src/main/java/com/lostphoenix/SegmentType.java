package com.lostphoenix;

public enum SegmentType {
    DARK_GREEN_S(Color.DG, Thickness.S),
    LIGHT_GREEN_S(Color.LG, Thickness.S),
    PINK_S(Color.PINK, Thickness.S),
    BLUE_S(Color.BLUE, Thickness.S),
    DARK_GREEN_M(Color.DG, Thickness.M),
    LIGHT_GREEN_M(Color.LG, Thickness.M),
    PINK_M(Color.PINK, Thickness.M),
    BLUE_M(Color.BLUE, Thickness.M),
    DARK_GREEN_L(Color.DG, Thickness.L),
    LIGHT_GREEN_L(Color.LG, Thickness.L),
    PINK_L(Color.PINK, Thickness.L),
    BLUE_L(Color.BLUE, Thickness.L),
    DARK_GREEN_XL(Color.DG, Thickness.XL),
    LIGHT_GREEN_XL(Color.LG, Thickness.XL),
    PINK_XL(Color.PINK, Thickness.XL),
    BLUE_XL(Color.BLUE, Thickness.XL);
    public final Color color;
    public final Thickness thickness;

    SegmentType(Color color, Thickness thickness) {
        this.color = color;
        this.thickness = thickness;
    }
}
