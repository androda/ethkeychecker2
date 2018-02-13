package com.lostphoenix;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lostphoenix.ColorProcessor.LegendShiftingColorProcessor;
import com.lostphoenix.ColorProcessor.SimpleColorProcessor;
import com.lostphoenix.RingStringGenerator.*;
import com.lostphoenix.ThicknessProcessor.LegendShiftingThicknessProcessor;
import com.lostphoenix.ThicknessProcessor.SimpleThicknessProcessor;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Application2 {


//    static Segment[] segments = new Segment[]{
//            new Segment(Color.DG, Thickness.S), new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.M), new Segment(Color.LG, Thickness.M),
//            new Segment(Color.PINK, Thickness.XL), new Segment(Color.LG, Thickness.L), new Segment(Color.DG, Thickness.XL), new Segment(Color.LG, Thickness.XL),
//            new Segment(Color.BLUE, Thickness.M), new Segment(Color.DG, Thickness.L), new Segment(Color.BLUE, Thickness.S), new Segment(Color.PINK, Thickness.M),
//            new Segment(Color.PINK, Thickness.L), new Segment(Color.PINK, Thickness.L), new Segment(Color.DG, Thickness.M), new Segment(Color.PINK, Thickness.L),
//            new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.S), new Segment(Color.PINK, Thickness.M), new Segment(Color.BLUE, Thickness.L),
//            new Segment(Color.DG, Thickness.XL), new Segment(Color.BLUE, Thickness.S), new Segment(Color.LG, Thickness.XL), new Segment(Color.LG, Thickness.XL),
//            new Segment(Color.BLUE, Thickness.S), new Segment(Color.DG, Thickness.L), new Segment(Color.PINK, Thickness.XL), new Segment(Color.BLUE, Thickness.M),
//            new Segment(Color.PINK, Thickness.L), new Segment(Color.BLUE, Thickness.XL), new Segment(Color.PINK, Thickness.M), new Segment(Color.LG, Thickness.S),
//            new Segment(Color.LG, Thickness.XL), new Segment(Color.PINK, Thickness.XL), new Segment(Color.BLUE, Thickness.L), new Segment(Color.LG, Thickness.L),
//            new Segment(Color.BLUE, Thickness.L), new Segment(Color.BLUE, Thickness.S), new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.M),
//            new Segment(Color.BLUE, Thickness.S), new Segment(Color.DG, Thickness.S), new Segment(Color.DG, Thickness.M), new Segment(Color.PINK, Thickness.S),
//            new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.L), new Segment(Color.LG, Thickness.S), new Segment(Color.BLUE, Thickness.XL),
//            new Segment(Color.BLUE, Thickness.XL), new Segment(Color.LG, Thickness.L), new Segment(Color.DG, Thickness.L), new Segment(Color.LG, Thickness.S),
//            new Segment(Color.LG, Thickness.XL), new Segment(Color.LG, Thickness.S), new Segment(Color.BLUE, Thickness.XL), new Segment(Color.BLUE, Thickness.XL),
//            new Segment(Color.PINK, Thickness.M), new Segment(Color.BLUE, Thickness.M), new Segment(Color.PINK, Thickness.L), new Segment(Color.LG, Thickness.M),
//            new Segment(Color.DG, Thickness.L), new Segment(Color.BLUE, Thickness.L), new Segment(Color.PINK, Thickness.S), new Segment(Color.DG, Thickness.L),
//    };


    //    static int[] hexOrder = new int[]{0, 5, 4, 5, 14, 9, 12, 13, 7, 8, 3, 6, 10, 10, 4, 10, 5, 0, 6, 11, 12, 3, 13, 13, 3, 8, 14, 7, 10, 15, 6, 1, 13, 14, 11, 9, 11, 3, 5, 4, 3, 0, 4, 2, 5, 8, 1, 15, 15, 9, 8, 1, 13, 1, 15, 15, 6, 7, 10, 5, 8, 11, 2, 8};
    public static SegmentType[] segmentOrder = new SegmentType[]{
            /*1*/SegmentType.DARK_GREEN_S, SegmentType.LIGHT_GREEN_M, SegmentType.DARK_GREEN_M, SegmentType.LIGHT_GREEN_M,
            /*2*/SegmentType.PINK_XL, SegmentType.LIGHT_GREEN_L, SegmentType.DARK_GREEN_XL, SegmentType.LIGHT_GREEN_XL,
            /*3*/SegmentType.BLUE_M, SegmentType.DARK_GREEN_L, SegmentType.BLUE_S, SegmentType.PINK_M,
            /*4*/SegmentType.PINK_L, SegmentType.PINK_L, SegmentType.DARK_GREEN_M, SegmentType.PINK_L,
            /*5*/SegmentType.LIGHT_GREEN_M, SegmentType.DARK_GREEN_S, SegmentType.PINK_M, SegmentType.BLUE_L,
            /*6*/SegmentType.DARK_GREEN_XL, SegmentType.BLUE_S, SegmentType.LIGHT_GREEN_XL, SegmentType.LIGHT_GREEN_XL,
            /*7*/SegmentType.BLUE_S, SegmentType.DARK_GREEN_L, SegmentType.PINK_XL, SegmentType.BLUE_M,
            /*8*/SegmentType.PINK_L, SegmentType.BLUE_XL, SegmentType.PINK_M, SegmentType.LIGHT_GREEN_S,
            /*9*/SegmentType.LIGHT_GREEN_XL, SegmentType.PINK_XL, SegmentType.BLUE_L, SegmentType.LIGHT_GREEN_L,
            /*10*/SegmentType.BLUE_L, SegmentType.BLUE_S, SegmentType.LIGHT_GREEN_M, SegmentType.DARK_GREEN_M,
            /*11*/SegmentType.BLUE_S, SegmentType.DARK_GREEN_S, SegmentType.DARK_GREEN_M, SegmentType.PINK_S,
            /*12*/SegmentType.LIGHT_GREEN_M, SegmentType.DARK_GREEN_L, SegmentType.LIGHT_GREEN_S, SegmentType.BLUE_XL,
            /*13*/SegmentType.BLUE_XL, SegmentType.LIGHT_GREEN_L, SegmentType.DARK_GREEN_L, SegmentType.LIGHT_GREEN_S,
            /*14*/SegmentType.LIGHT_GREEN_XL, SegmentType.LIGHT_GREEN_S, SegmentType.BLUE_XL, SegmentType.BLUE_XL,
            /*15*/ SegmentType.PINK_M, SegmentType.BLUE_M, SegmentType.PINK_L, SegmentType.LIGHT_GREEN_M,
            /*16*/ SegmentType.DARK_GREEN_L, SegmentType.BLUE_L, SegmentType.PINK_S, SegmentType.DARK_GREEN_L};




    //TODO: What if color/thickness bits are intertwined like c1t1c2t2 so color 01 and thickness 01 turns into the string 0011?




    public static List<String> inToOutTraversal(List<RingStringGenerator> generators) {
        List<String> retVal = Lists.newArrayList();

        for (int genIndex = 0; genIndex < generators.size(); genIndex++) {
            RingStringGenerator generator = generators.get(genIndex);
            StringBuilder normal = new StringBuilder();
            StringBuilder leftShiftBuilder = new StringBuilder();
            StringBuilder rightShiftBuilder = new StringBuilder();
            StringBuilder normalPrepend = new StringBuilder();
            StringBuilder leftShiftPrependBuilder = new StringBuilder();
            StringBuilder rightShiftPrependBuilder = new StringBuilder();
            for (int ring = 0; ring < 16; ring++) {
                for (int segment = 0; segment < 4; segment++) {
                    List<Coordinate> coordinates = Lists.newArrayList(
                            new Coordinate(ring, segment)

                    );
                    String ringChar = generator.generate(coordinates, segmentOrder);

                    String rightShift = convertBinaryStringToHex(rightRotate(ringChar, ring), 1);
                    String leftShift = convertBinaryStringToHex(leftRotate(ringChar, ring), 1);
                    normal.append(convertBinaryStringToHex(ringChar, 1));
                    normalPrepend.insert(0, convertBinaryStringToHex(ringChar, 1));
                    rightShiftBuilder.append(rightShift);
                    rightShiftPrependBuilder.insert(0, rightShift);
                    leftShiftBuilder.append(leftShift);
                    leftShiftPrependBuilder.insert(0, leftShift);


                }
            }
            retVal.add(normal.toString());
            retVal.add(leftShiftBuilder.toString());
            retVal.add(rightShiftBuilder.toString());
            retVal.add(normal.reverse().toString());
            retVal.add(leftShiftBuilder.reverse().toString());
            retVal.add(rightShiftBuilder.reverse().toString());
            retVal.add(normalPrepend.toString());
            retVal.add(rightShiftPrependBuilder.toString());
            retVal.add(leftShiftPrependBuilder.toString());
            retVal.add(normalPrepend.reverse().toString());
            retVal.add(rightShiftPrependBuilder.reverse().toString());
            retVal.add(leftShiftPrependBuilder.reverse().toString());
        }

        return retVal;
    }

    public static List<String> spiral(List<RingStringGenerator> generators) {
        List<String> retVal = Lists.newArrayList();

        for (int genIndex = 0; genIndex < generators.size(); genIndex++) {
            RingStringGenerator generator = generators.get(genIndex);
//            StringBuilder outToIn = new StringBuilder();
            List<String> spiralStrings = Lists.newArrayList();
            for (int segIndex = 0; segIndex < 4; segIndex++) {
                List<Coordinate> coordinates = Lists.newArrayList();
                for (int ring = 0; ring < 16; ring++) {
                    coordinates.add(new Coordinate(ring, segIndex));
                }
                spiralStrings.add(convertBinaryStringToHex(generator.generate(coordinates, segmentOrder), 16));
            }

            Collection<List<String>> spiralPermutations = Collections2.permutations(spiralStrings);
            for (List<String> spiralPermutation : spiralPermutations) {
                StringBuilder spiralString = new StringBuilder();
                for (String spiral : spiralPermutation) {
                    spiralString.append(spiral);
                }
                retVal.add(spiralString.toString());
                retVal.add(spiralString.reverse().toString());

            }

        }

        return retVal;
    }

//    public static List<String> subsectionXor(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
//        List<String> retVal = Lists.newArrayList();
//
//        for (int genIndex = 0; genIndex < generators.size(); genIndex++) {
//            RingStringGenerator generator = generators.get(genIndex);
//            StringBuilder normal = new StringBuilder();
//            StringBuilder normalPrepend = new StringBuilder();
//
//            String sectionString = null;
//
//                for (int subSection = 0; subSection < 4; subSection++) {
//                    List<Coordinate> coordinates = Lists.newArrayList();
//                    for (int segStartIndex = 0; segStartIndex < 4; segStartIndex++) {
//                        for (int ring = 0; ring < 16; ring++) {
//                        coordinates.add(new Coordinate(ring, ((segStartIndex * ((ring + subSection) / 4)) / 4) % 4));
//                    }
//                }
//
//
//                String ringChar = generator.generate(colorMap, thicknessMap, coordinates);
//                if(sectionString==null){
//                    sectionString=ringChar;
//                }else{
//                    sectionString = xorBinaryStrings(sectionString, ringChar);
//                }
//
//
//            }
//            normal.append(convertBinaryStringToHex(sectionString));
//            normalPrepend.insert(0, convertBinaryStringToHex(sectionString));
//            retVal.add(normal.toString());
//            retVal.add(normal.reverse().toString());
//            retVal.add(normalPrepend.toString());
//            retVal.add(normalPrepend.reverse().toString());
//
//        }
//
//        return retVal;
//    }

    private static String xorBinaryStrings(String s1, String s2) {
        if (s1.length() != s2.length())
            throw new IllegalArgumentException("Sizes must match");
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                retVal.append("0");
            } else {
                retVal.append("1");
            }
        }
        return retVal.toString();
    }

    private static String orBinaryStrings(String s1, String s2) {
        if (s1.length() != s2.length())
            throw new IllegalArgumentException("Sizes must match");
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == '1' || s2.charAt(i) == '1') {
                retVal.append("1");
            } else {
                retVal.append("0");
            }
        }
        return retVal.toString();
    }

    private static List<String> buildPossiblePrivateKeys(List<RingStringGenerator> generators) {
        List<String> retVal = Lists.newArrayList();
        retVal.addAll(inToOutTraversal(generators));
//        retVal.addAll(spiral(colorMap, thicknessMap));
//        retVal.addAll(subsectionXor(colorMap, thicknessMap));

        return retVal;

    }

    public static String convertBinaryStringToHex(String s, int size) {
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < s.length(); i += 4) {
            String section = s.substring(i, i + 4);
            String secitionConverted = Long.toString(Long.parseLong(section, 2), 16);
            while (secitionConverted.length() < size) {
                secitionConverted = "0" + secitionConverted;
            }
            retVal.append(secitionConverted);
        }
        return retVal.toString();
    }


    public static void brute() {

        String publicKey = "";
        String senderPrivKey = "";

        int count = 0;
        List<String> colors = Lists.newArrayList("00", "01", "10", "11");
        List<String> thicknesses = Lists.newArrayList("00", "01", "10", "11");
        Iterator<List<String>> colorIterator = Collections2.permutations(colors).iterator();
        boolean found = false;

        while (colorIterator.hasNext()) {
            List<String> colorList = colorIterator.next();
            Map<Color, String> colorMap = Maps.newHashMap();

            colorMap.put(Color.PINK, colorList.get(0));
            colorMap.put(Color.DG, colorList.get(1));
            colorMap.put(Color.BLUE, colorList.get(2));
            colorMap.put(Color.LG, colorList.get(3));
            Iterator<List<String>> thicknessIterator = Collections2.permutations(thicknesses).iterator();

            while (thicknessIterator.hasNext()) {
                List<String> thicknessList = thicknessIterator.next();
                Map<Thickness, String> thicknessMap = Maps.newHashMap();
                thicknessMap.put(Thickness.S, thicknessList.get(0));
                thicknessMap.put(Thickness.M, thicknessList.get(1));
                thicknessMap.put(Thickness.L, thicknessList.get(2));
                thicknessMap.put(Thickness.XL, thicknessList.get(3));

                List<RingStringGenerator> generators = Lists.newArrayList(
                        new CCTTRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                        new CTCTRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                        new TCTCRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                        new TTCCRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                        new TCTCIntertwinedRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                        new CTCTIntertwinedRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap))
                );

                List<String> privateKeys = buildPossiblePrivateKeys(generators);
                for (String privateKey : privateKeys) {
                    senderPrivKey = privateKey;
                    if (validate(privateKey)) {
                        System.out.println("FOUND!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(privateKey);
                        System.out.println(privateKey);
                        System.out.println(privateKey);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        found = true;
                        break;
                    }


                }


                if (count % 1000 == 0 || count < 100) {

                    System.out.println(count);
                    System.out.println(privateKeys.size());
                    for (String privateKey : privateKeys) {
                        System.out.println(privateKey);
                    }
                }
                count++;
            }
            if (found)
                break;

        }
        System.out.println(count);
        if (found) {
            System.out.println(senderPrivKey);
            System.out.println("Public key: " + publicKey);
        }
    }

    public static void main(String[] args) {
        printStatistics();



//        System.out.println(System.currentTimeMillis());
        brute();
//        System.out.println(System.currentTimeMillis());
//        simpleRun();
    }

    private static void simpleRun() {
        Map<Color, String> colorMap = Maps.newHashMap();
        Map<Thickness, String> thicknessMap = Maps.newHashMap();

        colorMap.put(Color.PINK, "10");
        colorMap.put(Color.DG, "00");
        colorMap.put(Color.BLUE, "11");
        colorMap.put(Color.LG, "01");
        thicknessMap.put(Thickness.S, "00");
        thicknessMap.put(Thickness.XL, "01");
        thicknessMap.put(Thickness.M, "10");
        thicknessMap.put(Thickness.L, "11");

        List<RingStringGenerator> generators = Lists.newArrayList(
                new CCTTRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                new CTCTRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                new TCTCRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                new TTCCRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                new TCTCIntertwinedRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap)),
                new CTCTIntertwinedRingStringGenerator(new LegendShiftingColorProcessor(colorMap), new LegendShiftingThicknessProcessor(thicknessMap))
        );
        for (String s : buildPossiblePrivateKeys(generators)) {
            System.out.println(s);
            validate(s);
        }
    }

    private static String leftRotate(String s, int shift) {
        int i = shift % s.length();
        return s.substring(i) + s.substring(0, i);
    }

    private static String rightRotate(String s, int shift) {
        int i = s.length() - (shift % s.length());
        return s.substring(i) + s.substring(0, i);

    }

    private static String getPublicFromPrivate(String senderPrivKey) {
        String publicKey;
        BigInteger pk = new BigInteger(senderPrivKey, 16);
        // System.out.println("Private key: " + pk.toString(16));

        ECKey key = ECKey.fromPrivate(pk);
        publicKey = Hex.toHexString(key.getAddress());
        return publicKey;
    }

    public static boolean validate(String privateKey) {
        String publicFromPrivate = getPublicFromPrivate(privateKey);
        return publicFromPrivate.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce");
    }

    public static void printStatistics() {
        Map<SegmentType, Integer> counts = Maps.newHashMap();

        Map<Color, Integer> colorCounts = Maps.newHashMap();

        Map<Thickness, Integer> thicknessCounts = Maps.newHashMap();
        for (SegmentType segmentType : segmentOrder) {
            Integer count = counts.get(segmentType);
            if (count == null) {
                count = 0;
            }
            counts.put(segmentType, count + 1);
            Integer colorCount = colorCounts.get(segmentType.color);
            if (colorCount == null) {
                colorCount = 0;
            }
            colorCounts.put(segmentType.color, colorCount + 1);
            Integer thicknessCount = thicknessCounts.get(segmentType.thickness);
            if (thicknessCount == null) {
                thicknessCount = 0;
            }
            thicknessCounts.put(segmentType.thickness, thicknessCount + 1);

        }

        System.out.println(counts);
        System.out.println(colorCounts);
        System.out.println(thicknessCounts);
    }


}
