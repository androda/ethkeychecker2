package com.androda;

import com.androda.solvers.KeyShiftNucleator;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shared.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

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
    private static final List<Segment> segments_reverseOrder;
    private static final List<List<Segment>> segmentsByRing_originalOrder = Lists.newArrayList();
    private static final List<List<Segment>> segmentsByRing_reverseOrder;
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
            segmentsByRing_originalOrder.add(segments);
        }
        segments_reverseOrder = new ArrayList<>(segmentsInOrder_contiguous);
        Collections.reverse(segments_reverseOrder);
        segmentsByRing_reverseOrder = new ArrayList<>(segmentsByRing_originalOrder.size());
        for (int i = 0; i < segmentsByRing_originalOrder.size(); i++) {
            segmentsByRing_reverseOrder.add(new ArrayList<>(segmentsByRing_originalOrder.get(i)));
            Collections.reverse(segmentsByRing_reverseOrder.get(i));
        }
        Collections.reverse(segmentsByRing_reverseOrder);
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

        KeyShiftNucleator keyShiftNucleator = new KeyShiftNucleator(utils, segmentsInOrder_contiguous, segmentsByRing_originalOrder);

        boolean performRingShift = true;
        boolean performKeyShift = true;
        final ForkJoinPool threadLimiterPool = new ForkJoinPool(4);
        try {
            Collections2.permutations(possibleBinaryValues).forEach(colorPermutation -> {
                Map<Color, String> colorMap = SolverUtils.colorMapFromBinaryValues(colorPermutation);
                Map<Color, String> invColorMap = SolverUtils.invertColorToBinaryStringMap(colorMap);
                Collections2.permutations(possibleBinaryValues).forEach(thicknessPermutation -> {
                    Map<Thickness, String> thicknessMap = SolverUtils.thicknessMapFromBinaryValues(thicknessPermutation);
                    Map<Thickness, String> invThicknessMap = SolverUtils.invertThicknessToBinaryStringMap(thicknessMap);
                    for (SolverUtils.SegOrder segOrder : SolverUtils.SegOrder.values()) {
                        if (performRingShift) {
                            IntStream.rangeClosed(0, 15).parallel().forEach(rotationDistance -> {
                                StringBuilder metadataBuilder = new StringBuilder();
                                String ringSoFar;

                                StringBuilder rotateRightATR = new StringBuilder(64);
                                StringBuilder rotateRightATL = new StringBuilder(64);

                                StringBuilder rotateLeftATR = new StringBuilder(64);
                                StringBuilder rotateLeftATL = new StringBuilder(64);
                                for (int f = 0; f < segmentsByRing_originalOrder.size(); f++) {
                                    rotateRightATR.append(SolverUtils.ringToString(colorMap, invColorMap, thicknessMap, invThicknessMap, segmentsByRing_originalOrder.get(f), segOrder));
                                    rotateLeftATR.append(SolverUtils.ringToString(colorMap, invColorMap, thicknessMap, invThicknessMap, segmentsByRing_originalOrder.get(f), segOrder));
                                    rotateRightATL.insert(0, SolverUtils.ringToString(colorMap, invColorMap, thicknessMap, invThicknessMap, segmentsByRing_originalOrder.get(f), segOrder));
                                    rotateLeftATL.insert(0, SolverUtils.ringToString(colorMap, invColorMap, thicknessMap, invThicknessMap, segmentsByRing_originalOrder.get(f), segOrder));
                                    if (rotationDistance != 0) {
                                        ringSoFar = rotateRightATR.toString();
                                        rotateRightATR.delete(0, rotateRightATR.length());
                                        rotateRightATR.append(SolverUtils.rightRotate(ringSoFar, rotationDistance));

                                        ringSoFar = rotateRightATL.toString();
                                        rotateRightATL.delete(0, rotateRightATL.length());
                                        rotateRightATL.append(SolverUtils.rightRotate(ringSoFar, rotationDistance));

                                        ringSoFar = rotateLeftATR.toString();
                                        rotateLeftATR.delete(0, rotateLeftATR.length());
                                        rotateLeftATR.append(SolverUtils.leftRotate(ringSoFar, rotationDistance));

                                        ringSoFar = rotateLeftATL.toString();
                                        rotateLeftATL.delete(0, rotateLeftATL.length());
                                        rotateLeftATL.append(SolverUtils.leftRotate(ringSoFar, rotationDistance));
                                    }
                                }
                                metadataBuilder.append(rotationDistance).append('|')
                                        .append(segOrder.name()).append('|')
                                        .append("RS").append('|');
                                utils.validateBinaryPk(rotateRightATR.toString(), "RR_ATR|" + metadataBuilder.toString());
                                utils.validateBinaryPk(rotateRightATL.toString(), "RR_ATL|" + metadataBuilder.toString());
                                utils.validateBinaryPk(rotateLeftATR.toString(), "RL_ATR|" + metadataBuilder.toString());
                                utils.validateBinaryPk(rotateLeftATL.toString(), "RL_ATL|" + metadataBuilder.toString());
                            });


                        /*
                        Append the ring, then rotate the *entire ring string so far*
                        Rotate the ring, then append
                        Rotate the ring based on the number it is from the core
                        Rotate the ring just once each time
                        Maybe the first ring is not rotated

                         */

                        }

                        if (performKeyShift) {
//                                keyShiftNucleator.setRightOrLeftAppend(rightOrLeftAppend);
//                                keyShiftNucleator.setRightOrLeftShift(rightOrLeftShift);
//                                keyShiftNucleator.setSegOrder(segOrder);
//                                for (KeyShiftNucleator.KeyShiftMode keyShiftMode : KeyShiftNucleator.KeyShiftMode.values()) {
//                                    keyShiftNucleator.setKeyShiftMode(keyShiftMode);
//                                    keyShiftNucleator.nucleate(colorToBinaryMap, thicknessToBinaryMap);
//                                }
                        }
                    }
                });
            });
        } finally {
            utils.shutdownLoggers();
        }
    }

}
