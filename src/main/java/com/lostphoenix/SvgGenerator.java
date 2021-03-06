package com.lostphoenix;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class SvgGenerator {

    static Set<SegmentType> mostFrequentSegments = Sets.newHashSet(
//            SegmentType.LIGHT_GREEN_M,
//            SegmentType.DARK_GREEN_L
            SegmentType.BLUE_S,
            SegmentType.BLUE_XL,
            SegmentType.LIGHT_GREEN_XL,
            SegmentType.PINK_L

    );

    public static void main(String[] args) {

        try (PrintWriter out = new PrintWriter(new FileWriter("generated.svg"))){
            out.println("<svg id=\"svg\" width=\"850\" height=\"850\" xmlns=\"http://www.w3.org/2000/svg\">");
            out.println("<rect x=\"0\" y=\"0\" width=\"850\" height=\"850\"/>");
            for (int ring = 0; ring < 16; ring++) {
                for (int segment = 0; segment < 4; segment++) {
                    SegmentType segmentType = Ring.segmentOrder[ring * 4 + segment];
                    out.println("<path d=\""+getDAttributeWhiteSpaceTop(ring, segment)+"\" style=\"stroke: rgb(64,64,64) ; stroke-width: 5; fill:none;\"></path>");
                    out.println("<path d=\""+getDAttributeWhiteSpaceBottom(ring, segment)+"\" style=\"stroke: rgb(64,64,64) ; stroke-width: 5; fill:none;\"></path>");
                    if(mostFrequentSegments.contains(segmentType))

                    out.println("<path d=\"" + getDAttribute(ring, segment) + "\" style=\"stroke: " + colorString.get(segmentType.color) + "; stroke-width: " + thicknessString.get(segmentType.thickness) + "; fill:none;\"></path>");


                }
            }
            out.println("</svg>");

        } catch (IOException e) {

        }
    }

    public static String getDAttribute(int ring, int segment) {
        return String.format("M %s %s A %s %s 0 0 0 %s %s", xStartOffset(ring, segment), yStartOffset(ring, segment), 25 + ring * 25, 25 + ring * 25, xEndPosition(ring, segment), yEndPosition(ring, segment));
    }

    public static String getDAttributeWhiteSpaceTop(int ring, int segment) {
        return String.format("M %s %s A %s %s 0 0 0 %s %s", (425+(38+25*ring)), 425, 25 + ring * 25, 25 + ring * 25, (425-(38+25*ring)), 425);

    }

    public static String getDAttributeWhiteSpaceBottom(int ring, int segment) {
        return String.format("M %s %s A %s %s 0 0 0 %s %s", (425-(38+25*ring)), 425, 25 + ring * 25, 25 + ring * 25, (425+(38+25*ring)), 425);

    }
        public static double xStartOffset(int ring, int segment) {
        return 425 + ((25 + (25 * ring)) * Math.cos((ring + (4 * segment)) * (Math.PI / 8.0)));
    }

    public static double yStartOffset(int ring, int segment) {
        return 425 + ((25 + (25 * ring)) * Math.sin((ring + (4 * segment)) * (Math.PI / 8.0)));
    }

    public static double xEndPosition(int ring, int segment) {
        return 425 + ((25 + (25 * ring)) * Math.cos((ring + (4 * segment) - 4) * (Math.PI / 8.0)));
    }

    public static double yEndPosition(int ring, int segment) {
        return 425 + ((25 + (25 * ring)) * Math.sin((ring + (4 * segment) - 4) * (Math.PI / 8.0)));
    }

    static Map<Color, String> colorString = ImmutableMap.of(Color.DG, "rgb(51, 85, 51)", Color.LG, "rgb(102, 136, 68)", Color.BLUE, "rgb(68, 102, 136)", Color.PINK, "rgb(136, 68, 102)");
    static Map<Thickness, String> thicknessString = ImmutableMap.of(Thickness.S, "5", Thickness.M, "10", Thickness.L, "15", Thickness.XL, "20");


    /*
    <path d="M 420 400 A 20 20 0 0 0 400 380" style="stroke: rgb(51, 85, 51); stroke-width: 5; fill: none;"></path>
	<path d="M 400 420 A 20 20 0 0 0 420 400" style="stroke: rgb(102, 136, 68); stroke-width: 10; fill: none;"></path>
	<path d="M 380 400 A 20 20 0 0 0 400 420" style="stroke: rgb(51, 85, 51); stroke-width: 10; fill: none;"></path>

4
20
4
20
4
     */
}
