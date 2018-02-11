package com.shared;

public class Segment {
    Color color;
    Thickness thickness;

    public Segment(Color color, Thickness thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public String toString() {
        return (color == null ? "nc" : color.name()) + (thickness == null ? "nt" : thickness.name());
    }

}
