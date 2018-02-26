package com.androda;

import com.androda.solvers.KeyShiftNucleator;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shared.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ApplicationForked {

    protected static List<Boolean> trueFalse = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE);

    private static List<String> possibleBinaryValues = Lists.newArrayList("00", "01", "10", "11");

    private static final List<List<String>> segmentOrderByRing = Lists.newArrayList(
            Lists.newArrayList("D1","L2","D2","L2"),
            Lists.newArrayList("P4","L3","D4","L4"),
            Lists.newArrayList("B2","D3","B1","P2"),
            Lists.newArrayList("P3","P3","D2","P3"),
            Lists.newArrayList("L2","D1","P2","B3"),
            Lists.newArrayList("D4","B1","L4","L4"),
            Lists.newArrayList("B1","D3","P4","B2"),
            Lists.newArrayList("P3","B4","P2","L1"),
            Lists.newArrayList("L4","P4","B3","L3"),
            Lists.newArrayList("B3","B1","L2","D2"),
            Lists.newArrayList("B1","D1","D2","P1"),
            Lists.newArrayList("L2","D3","L1","B4"),
            Lists.newArrayList("B4","L3","D3","L1"),
            Lists.newArrayList("L4","L1","B4","B4"),
            Lists.newArrayList("P2","B2","P3","L2"),
            Lists.newArrayList("D3","B3","P1","D3"));
    private static final List<String> allSegmentsInOrder_string = Lists.newArrayList();
    static final Map<Integer, Set<String>> frequencyToSegmentType = Maps.newHashMap();
    static final Map<String, Integer> segmentTypeToFrequency = Maps.newHashMap();

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
                allSegmentsInOrder_string.add(segment);
                segmentTypeToFrequency.put(segment, (segmentTypeToFrequency.getOrDefault(segment, 0) + 1));

                switch (segment.charAt(0)) {
                    case 'D':
                        c = Color.DG;
                        break;
                    case 'L':
                        c = Color.LG;
                        break;
                    case 'P':
                        c = Color.PINK;
                        break;
                    case 'B':
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

        for (Map.Entry<String, Integer> segToFrequency : segmentTypeToFrequency.entrySet()) {
            Set<String> segmentsOccurring = frequencyToSegmentType.getOrDefault(segToFrequency.getValue(), Sets.newHashSet());
            segmentsOccurring.add(segToFrequency.getKey());
            frequencyToSegmentType.put(segToFrequency.getValue(), segmentsOccurring);
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

    static final String hexabet = "0123456789abcdef";
    static String filePath = "C:\\%s\\ethkeychecker2\\allKeysTried.txt";

    public static void main(String[] args) {
        filePath = String.format(filePath, args[0]);
        SolverUtils utils = new SolverUtils(filePath);

        KeyShiftNucleator keyShiftNucleator = new KeyShiftNucleator(utils, segmentsInOrder_contiguous, segmentsByRing_originalOrder);

        boolean performRingShift = true;
        boolean performKeyShift = false;
        boolean performFrequencyAnalysisKeyGen = true;

        try {
            if (performFrequencyAnalysisKeyGen) {
                // Permute through in frequency order
                Collections2.permutations(frequencyToSegmentType.get(2)).forEach(twoPerm -> {
                    Collections2.permutations(frequencyToSegmentType.get(3)).parallelStream().forEach(threePerm -> {
                        Collections2.permutations(frequencyToSegmentType.get(4)).forEach(fourPerm -> {
                            Collections2.permutations(frequencyToSegmentType.get(5)).forEach(fivePerm -> {
                                Collections2.permutations(frequencyToSegmentType.get(6)).forEach(sixPerm -> {
                                    Permutations.of(twoPerm, threePerm, fourPerm, fivePerm, sixPerm)
                                    .map(q -> q.collect(Collectors.toList()))
                                    .forEach(lls -> {
                                        Map<String, Character> hexabetMap = Maps.newHashMap();
                                        AtomicInteger i = new AtomicInteger(0);
                                        for (int rotateBy = 0; rotateBy < 16; rotateBy++) {
                                            StringBuilder keyBuilder = new StringBuilder(64);
                                            String rotatedHexabet = SolverUtils.rightRotate(hexabet, rotateBy);
                                            for (List<String> segs : lls) {
                                                segs.forEach(seg -> hexabetMap.put(seg, rotatedHexabet.charAt(i.getAndIncrement())));
                                            }

                                            for (String seg : allSegmentsInOrder_string) {
                                                keyBuilder.append(hexabetMap.get(seg));
                                            }

                                            utils.validateHexPk(keyBuilder.toString(), null);

                                            i.set(0);
                                            hexabetMap.clear();
                                        }
                                    });
                                });
                            });
                        });
                    });
                });
            }

            if (performKeyShift || performRingShift) {
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
            }
        } finally {
            utils.shutdownLoggers();
            System.out.println("Finished at " + Instant.now().toString());
            if (SolverUtils.numPkChecked != null) {
                System.out.println(SolverUtils.numPkChecked.get() + " private keys checked");
            }
        }
    }

}
