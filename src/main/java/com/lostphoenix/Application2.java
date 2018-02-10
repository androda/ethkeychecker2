package com.lostphoenix;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
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
    static SegmentType[] segmentOrder = new SegmentType[]{
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

    private static class CTCTRingStringGenerator implements RingStringGenerator {
        public String generate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Coordinate> segments) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(colorMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].color));
                binaryString.append(thicknessMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].thickness));
            }
            return binaryString.toString();
        }
    }

    private static class TCTCRingStringGenerator implements RingStringGenerator {
        public String generate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Coordinate> segments) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(thicknessMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].thickness));
                binaryString.append(colorMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].color));
            }
            return binaryString.toString();
        }
    }

    private static class CCTTRingStringGenerator implements RingStringGenerator {
        public String generate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Coordinate> segments) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(colorMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].color));
            }
            for (Coordinate segment : segments) {
                binaryString.append(thicknessMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].thickness));
            }
            return binaryString.toString();
        }
    }


    private static class TTCCRingStringGenerator implements RingStringGenerator {
        public String generate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Coordinate> segments) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(thicknessMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].thickness));
            }
            for (Coordinate segment : segments) {
                binaryString.append(colorMap.get(segmentOrder[segment.ring * 4 + segment.segmentInRing].color));
            }
            return binaryString.toString();
        }
    }

    //TODO: What if color/thickness bits are intertwined like c1t1c2t2 so color 01 and thickness 01 turns into the string 0011?


    private static interface RingStringGenerator {
        String generate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Coordinate> segments);
    }

    private static class Coordinate {
        public final int ring;
        public final int segmentInRing;

        public Coordinate(int ring, int segment) {
            this.ring = ring;
            this.segmentInRing = segment;
        }
    }

    private static List<RingStringGenerator> generators = Lists.newArrayList(new CCTTRingStringGenerator(), new CTCTRingStringGenerator(), new TCTCRingStringGenerator(), new TTCCRingStringGenerator());

    public static List<String> inToOutTraversal(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
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
                List<Coordinate> coordinates = Lists.newArrayList(
                        new Coordinate(ring, 0),
                        new Coordinate(ring, 1),
                        new Coordinate(ring, 2),
                        new Coordinate(ring, 3)

                );


                String ringChar = generator.generate(colorMap, thicknessMap, coordinates);

                String rightShift = rightRotate(ringChar, ring);
                String leftShift = leftRotate(ringChar, ring);
                normal.append(convertBinaryStringToHex(ringChar));
                normalPrepend.insert(0, convertBinaryStringToHex(ringChar));
                rightShiftBuilder.append(convertBinaryStringToHex(rightShift));
                rightShiftPrependBuilder.insert(0, convertBinaryStringToHex(rightShift));
                leftShiftBuilder.append(convertBinaryStringToHex(leftShift));
                leftShiftPrependBuilder.insert(0, convertBinaryStringToHex(leftShift));
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

    public static List<String> spiralIntoOut(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
        List<String> retVal = Lists.newArrayList();

        for (int genIndex = 0; genIndex < generators.size(); genIndex++) {
            RingStringGenerator generator = generators.get(genIndex);
            StringBuilder normal = new StringBuilder();
            StringBuilder normalPrepend = new StringBuilder();
            List<Coordinate> coordinates = Lists.newArrayList();
            for (int segIndex = 0; segIndex < 4; segIndex++) {
                for (int ring = 0; ring < 16; ring++) {
                    coordinates.add(new Coordinate(ring, segIndex));
                }
            }

            String ringChar = generator.generate(colorMap, thicknessMap, coordinates);

            normal.append(convertBinaryStringToHex(ringChar));
            normalPrepend.insert(0, convertBinaryStringToHex(ringChar));
            retVal.add(normal.toString());
            retVal.add(normal.reverse().toString());
            retVal.add(normalPrepend.toString());
            retVal.add(normalPrepend.reverse().toString());
        }

        return retVal;
    }


    private static List<String> buildPossiblePrivateKeys(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
        List<String> retVal = Lists.newArrayList();
        retVal.addAll(inToOutTraversal(colorMap, thicknessMap));
        retVal.addAll(spiralIntoOut(colorMap,thicknessMap));
        return retVal;

    }

    public static String convertBinaryStringToHex(String s) {
        StringBuilder retVal = new StringBuilder();
        for(int i=0;i<s.length();i+=16) {
            String section = s.substring(i,i+16);
            String secitionConverted = Long.toString(Long.parseLong(section, 2), 16);
            while (secitionConverted.length() < 4) {
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
            Map<Color, String> localColorMap = Maps.newHashMap();

            localColorMap.put(Color.PINK, colorList.get(0));
            localColorMap.put(Color.DG, colorList.get(1));
            localColorMap.put(Color.BLUE, colorList.get(2));
            localColorMap.put(Color.LG, colorList.get(3));
            Iterator<List<String>> thicknessIterator = Collections2.permutations(thicknesses).iterator();

            while (thicknessIterator.hasNext()) {
                List<String> thicknessList = thicknessIterator.next();
                Map<Thickness, String> localThicknessMap = Maps.newHashMap();
                localThicknessMap.put(Thickness.S, thicknessList.get(0));
                localThicknessMap.put(Thickness.M, thicknessList.get(1));
                localThicknessMap.put(Thickness.L, thicknessList.get(2));
                localThicknessMap.put(Thickness.XL, thicknessList.get(3));

                List<String> privateKeys = buildPossiblePrivateKeys(localColorMap, localThicknessMap);
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
        System.out.println(senderPrivKey);
        System.out.println("Public key: " + publicKey);
    }

    public static void main(String[] args) {

        brute();
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
        thicknessMap.put(Thickness.M, "01");
        thicknessMap.put(Thickness.L, "10");
        thicknessMap.put(Thickness.XL, "11");
        for (String s : buildPossiblePrivateKeys(colorMap, thicknessMap)) {
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


}
