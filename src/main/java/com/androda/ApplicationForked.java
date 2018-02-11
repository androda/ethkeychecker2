package com.androda;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shared.Color;
import com.shared.Segment;
import com.shared.SolverUtils;
import com.shared.Thickness;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApplicationForked {


    private static Map<Color, String> colorToBinaryMap = Maps.newHashMap();
    private static Map<Thickness, String> thicknessToBinaryMap = Maps.newHashMap();
    static {
        thicknessToBinaryMap.put(Thickness.S, "00");
        thicknessToBinaryMap.put(Thickness.M, "01");
        thicknessToBinaryMap.put(Thickness.L, "10");
        thicknessToBinaryMap.put(Thickness.XL, "11");

        colorToBinaryMap.put(Color.PINK, "00");
        colorToBinaryMap.put(Color.DG, "01");
        colorToBinaryMap.put(Color.BLUE, "10");
        colorToBinaryMap.put(Color.LG, "11");
    }

    private static List<String> thicknessBinary = Lists.newArrayList("00", "01", "11", "10");
    private static List<String> colorBinary = Lists.newArrayList("00", "01", "11", "10");

    private static final List<List<String>> segmentOrderByRing = Lists.newArrayList(
            Lists.newArrayList("A1","B2","A2","B2"),
            Lists.newArrayList("C4","B3","A4","B4"),
            Lists.newArrayList("D2","A3","D1","C2"),
            Lists.newArrayList("C3","C3","A2","C3"),
            Lists.newArrayList("B2","A1","C2","D3"),
            Lists.newArrayList("A4","D1","B4","B4"),
            Lists.newArrayList("D1","A3","C4","D2"),
            Lists.newArrayList("C3","D4","C2","B1"),
            Lists.newArrayList("B4","C4","D3","B3"),
            Lists.newArrayList("D3","D1","B2","A2"),
            Lists.newArrayList("D1","A1","A2","C1"),
            Lists.newArrayList("B2","A3","B1","D4"),
            Lists.newArrayList("D4","B3","A3","B1"),
            Lists.newArrayList("B4","B1","D4","D4"),
            Lists.newArrayList("C2","D2","C3","B2"),
            Lists.newArrayList("A3","D3","C1","A3"));

    private static final List<Segment> segmentsInOrder_contiguous = Lists.newArrayList();
    private static final List<List<Segment>> segmentsInOrder = Lists.newArrayList();
    static {
        for (List<String> ringSegments : segmentOrderByRing) {
            List<Segment> segments;
            Color c = null;
            Thickness t = null;

            segments = Lists.newArrayList();
            for (String segment : ringSegments) {
                switch (segment.charAt(0)) {
                    case 'A':
                        c = Color.DG;
                        break;
                    case 'B':
                        c = Color.LG;
                        break;
                    case 'C':
                        c = Color.PINK;
                        break;
                    case 'D':
                        c = Color.BLUE;
                        break;
                }
                switch (segment.charAt(1)) {
                    case '1':
                        t = Thickness.S;
                        break;
                    case '2':
                        t = Thickness.M;
                        break;
                    case '3':
                        t = Thickness.L;
                        break;
                    case '4':
                        t = Thickness.XL;
                        break;
                }
                Segment s = new Segment(c, t);
                segmentsInOrder_contiguous.add(s);
                segments.add(s);
            }
            segmentsInOrder.add(segments);
        }
    }

    // iterate through the segment values
    // stick into the 'this row' buffer the binary from this row
    // for the initial row, maybe don't rotate?
    // two options:
    // either rotate to the left or right before appending to the output buffer
    // OR
    // either rotate the output buffer to the left or right before or after appending the new row


    /*
    The Main method needs to have a loop which passes in all the possible combinations of 0s and 1s for each color and width
    But that's a LOT of possibilities.  16 for color, 16 for width, so 16!.  Gotta narrow it down.
    Start with the ones you think are most likely
    The one we came up with at work
    And then reverse line width
    As far as I know, ETH private keys can't start with 00



     */

    private interface Nucleator {
        /**
         * Performs several permutations (or just one) against the specified color and thickness maps
         */
        void nucleate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap);
    }

    private static class NucleatorBase {
        protected List<Segment> segments_originalOrder;
        protected List<Segment> segments_reverseOrder;

        protected List<Boolean> trueFalse = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE);

        protected SolverUtils.SegOrder segOrder;
        protected SolverUtils utils;

        protected final int SEG_PER_RING = 4;
        protected final int NUM_RINGS = 16;

        public NucleatorBase(SolverUtils utils, List<Segment> segments) {
            segments_originalOrder = ImmutableList.copyOf(segments);
            segments_reverseOrder = Lists.newArrayList(segments);
            Collections.reverse(segments_reverseOrder);
            this.utils = utils;
        }

        protected List<StringBuilder> makeReturnBuilderList(int numBuilders) {
            List<StringBuilder> builders = Lists.newArrayList();
            for (int i = 0; i < numBuilders; i++) {
                builders.add(new StringBuilder());
            }
            return builders;
        }

        protected List<String> convertToResponseStrings(List<StringBuilder> builders) {
            List<String> responses = Lists.newArrayListWithExpectedSize(builders.size());
            builders.forEach(builder -> responses.add(builder.toString()));
            return responses;
        }

        protected List<List<Segment>> getForwardSegmentsByRing() {
            return Lists.partition(segments_originalOrder, SEG_PER_RING);
        }

        protected List<List<Segment>> getReverseSegmentsByRing() {
            return Lists.partition(segments_reverseOrder, SEG_PER_RING);
        }

    }

    private static class ShiftNucleator extends NucleatorBase implements Nucleator {
        private List<Segment> segments_originalOrder;
        private List<Segment> segments_reverseOrder;

        boolean rightOrLeftAppend;
        boolean rightOrLeftShift;

        private int rotationDistance;

        final int ARTR = 0;  // append ring then rotate the whole string so far
        final int RRTA = 1;  // rotate ring then append
        final int RBDFCTA = 2;  // rotate based on distance from core then append
        final int ARTRBDFC = 3;  // append ring then rotate based on distance from core

        public ShiftNucleator(SolverUtils utils, List<Segment> segments) {
            super(utils, segments);
        }

        public void setRotationDistance(int rotationDistance) {
            this.rotationDistance = rotationDistance;
        }

        void appendThenRotate(List<Segment> ring, StringBuilder existing) {
            String ringString = SolverUtils.ringToString(colorToBinaryMap, thicknessToBinaryMap, ring, segOrder);
            String ringSoFar;
            if (rightOrLeftAppend) {
                existing.append(ringString);
                ringSoFar = existing.toString();
            } else {
                existing.insert(0, ringString);
                ringSoFar = existing.toString();
            }

            existing.delete(0, existing.length());
            if (rightOrLeftShift) {
                existing.append(SolverUtils.rightRotate(ringSoFar, rotationDistance));
            } else {
                existing.append(SolverUtils.leftRotate(ringSoFar, rotationDistance));
            }
        }


        @Override
        public void nucleate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
            List<List<Segment>> forwardRings = getForwardSegmentsByRing();
            List<List<Segment>> reverseRings = getReverseSegmentsByRing();

            for (int i = 0; i < 17; i++) {
                this.rotationDistance = i;
                for (SolverUtils.SegOrder order : SolverUtils.SegOrder.values()) {
                    this.segOrder = order;
                    for (Boolean ROLA : trueFalse) {
                        this.rightOrLeftAppend = ROLA;
                        for (Boolean ROLS : trueFalse) {
                            this.rightOrLeftShift = ROLS;
                            StringBuilder metadataBuilder = new StringBuilder();
                            metadataBuilder.append(rotationDistance).append('|')
                                    .append(segOrder.name()).append('|')
                                    .append(rightOrLeftAppend ? '1' : '0').append('|')
                                    .append(rightOrLeftShift ? '1' : '0').append('|');

                            StringBuilder forwardAtr = new StringBuilder(64);
                            for (int f = 0; f < forwardRings.size(); f++) {
                                appendThenRotate(forwardRings.get(f), forwardAtr);


                            }
                            utils.validateBinaryPk(forwardAtr.toString(), metadataBuilder.toString());

                            StringBuilder reverseAtr = new StringBuilder(64);
                            for (int r = 0; r < reverseRings.size(); r++) {
                                appendThenRotate(reverseRings.get(r), reverseAtr);


                            }
                            utils.validateBinaryPk(reverseAtr.toString(), metadataBuilder.toString());
                            int five = 7;
                        }

                    }

                }
            }



            // try 0 through 32 right rotations per ring

            /*
            Append the ring, then rotate the *entire ring string so far*
            Rotate the ring, then append
            Rotate the ring based on the number it is from the core
            Rotate the ring just once each time
            Maybe the first ring is not rotated

             */


        }
    }


    static final String lineSeparator = System.lineSeparator();
    static String filePath = "C:\\%s\\ethkeychecker2\\allKeysTried.txt";

    public static void main(String[] args) {
        filePath = String.format(filePath, args[0]);
        SolverUtils utils = new SolverUtils(filePath);

        ShiftNucleator shiftNucleator = new ShiftNucleator(utils, segmentsInOrder_contiguous);
        shiftNucleator.nucleate(colorToBinaryMap, thicknessToBinaryMap);



//
//        StringBuilder outputBuilder = new StringBuilder();
//        StringBuilder ringBuilder;
//
//        for (List<Segment> ring : segmentsInOrder) {
//            ringBuilder = new StringBuilder();
//            for (Segment segment : ring) {
////                ringBuilder.append()
////                ringBuilder.append(colorToBinaryMap.get(segment.color)).append(thicknessToBinaryMap.get(segment.thickness));
//            }
//
//            String ringString = Long.toString(Long.parseLong(ringBuilder.toString(), 2), 16);
//            outputBuilder.append(ringString);
//            writeToLogFile(fileWriter, ringString);
//        }

        int five = 7;

    }

    static void writeToLogFile(FileWriter writer, String key) {
        // todo should be batch file writing
        try {
            writer.write(key + lineSeparator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
