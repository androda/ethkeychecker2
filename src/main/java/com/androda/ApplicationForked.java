package com.androda;

import com.androda.solvers.KeyShiftNucleator;
import com.androda.solvers.RingShiftNucleator;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.shared.*;

import java.util.List;
import java.util.Map;

public class ApplicationForked {

    protected static List<Boolean> trueFalse = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE);

    private static List<String> possibleBinaryValues = Lists.newArrayList("00", "01", "10", "11");

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

    static String filePath = "C:\\%s\\ethkeychecker2\\allKeysTried.txt";

    public static void main(String[] args) {
        filePath = String.format(filePath, args[0]);
        SolverUtils utils = new SolverUtils(filePath);

        RingShiftNucleator ringShiftNucleator = new RingShiftNucleator(utils, segmentsInOrder_contiguous);
        KeyShiftNucleator keyShiftNucleator = new KeyShiftNucleator(utils, segmentsInOrder_contiguous);


        boolean performRingShift = true;
        boolean performKeyShift = false;

        Collections2.permutations(possibleBinaryValues).forEach(colorPermutation -> {
            Map<Color, String> colorToBinaryMap = SolverUtils.colorMapFromBinaryValues(colorPermutation);
            Collections2.permutations(possibleBinaryValues).forEach(thicknessPermutation -> {
                Map<Thickness, String> thicknessToBinaryMap = SolverUtils.thicknessMapFromBinaryValues(thicknessPermutation);

                for (Boolean rightOrLeftShift : trueFalse) {
                    for (Boolean rightOrLeftAppend : trueFalse) {
                        for (SolverUtils.SegOrder segOrder : SolverUtils.SegOrder.values()) {
                            if (performRingShift) {
                                ringShiftNucleator.setRightOrLeftAppend(rightOrLeftAppend);
                                ringShiftNucleator.setRightOrLeftShift(rightOrLeftShift);
                                ringShiftNucleator.setSegOrder(segOrder);
                                for (int rotationDistance = 0; rotationDistance < 17; rotationDistance++) {
                                    ringShiftNucleator.setRotationDistance(rotationDistance);
                                    ringShiftNucleator.nucleate(colorToBinaryMap, thicknessToBinaryMap);
                                }
                            }

                            if (performKeyShift) {
                                keyShiftNucleator.setRightOrLeftAppend(rightOrLeftAppend);
                                keyShiftNucleator.setRightOrLeftShift(rightOrLeftShift);
                                keyShiftNucleator.setSegOrder(segOrder);
                                for (KeyShiftNucleator.KeyShiftMode keyShiftMode : KeyShiftNucleator.KeyShiftMode.values()) {
                                    keyShiftNucleator.setKeyShiftMode(keyShiftMode);
                                    keyShiftNucleator.nucleate(colorToBinaryMap, thicknessToBinaryMap);
                                }
                            }
                        }
                    }
                }
            });
        });

        utils.shutdownLoggers();
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


    }

}
