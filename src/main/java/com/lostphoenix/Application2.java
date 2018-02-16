package com.lostphoenix;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lostphoenix.ColorProcessor.LegendShiftingColorProcessor;
import com.lostphoenix.RingStringGenerator.*;
import com.lostphoenix.ThicknessProcessor.LegendShiftingThicknessProcessor;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Application2 {
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
            List<String> ringBinaryStrings = Lists.newArrayList();
            for (int ring = 0; ring < 16; ring++) {
                List<Coordinate> coordinates = Lists.newArrayList();
                for (int segment = 0; segment < 4; segment++) {
                    coordinates.add(new Coordinate(ring, segment));
                }

                String ringStringBinary = generator.generate(coordinates, Ring.segmentOrder);
                ringBinaryStrings.add(ringStringBinary);
                //Bit shift segments left and right
                String rightShift = StringUtils.convertBinaryStringToHex(StringUtils.rightRotate(ringStringBinary, ring));
                String leftShift = StringUtils.convertBinaryStringToHex(StringUtils.leftRotate(ringStringBinary, ring));
                normal.append(StringUtils.convertBinaryStringToHex(ringStringBinary));

                normalPrepend.insert(0, StringUtils.convertBinaryStringToHex(ringStringBinary));

                rightShiftBuilder.append(rightShift);
                rightShiftPrependBuilder.insert(0, rightShift);
                leftShiftBuilder.append(leftShift);
                leftShiftPrependBuilder.insert(0, leftShift);


            }

            // Generate sliced keys

            retVal.addAll(generateSliceStrings(ringBinaryStrings));

//            retVal.add(normal.toString());
//            retVal.add(leftShiftBuilder.toString());
//            retVal.add(rightShiftBuilder.toString());
//            retVal.add(normal.reverse().toString());
//            retVal.add(leftShiftBuilder.reverse().toString());
//            retVal.add(rightShiftBuilder.reverse().toString());
//            retVal.add(normalPrepend.toString());
//            retVal.add(rightShiftPrependBuilder.toString());
//            retVal.add(leftShiftPrependBuilder.toString());
//            retVal.add(normalPrepend.reverse().toString());
//            retVal.add(rightShiftPrependBuilder.reverse().toString());
//            retVal.add(leftShiftPrependBuilder.reverse().toString());
        }

        return retVal;
    }

    private static List<String> generateSliceStrings(List<String> ringBinaryStrings) {
        List<String> sliceStrings = Lists.newArrayList();
        List<String> inToOutNoShift = Lists.newArrayList();
        List<String> outToInNoShift = Lists.newArrayList();
        for (int slice = 0; slice < 16; slice++) {
            StringBuilder sliceString = new StringBuilder();
            for (int ring = 0; ring < 16; ring++) {
                //String of 16 bits
                String ringString = ringBinaryStrings.get(ring);
                sliceString.append(ringString.charAt((16 + slice - ring) % 16));
            }
            inToOutNoShift.add(sliceString.toString());
            outToInNoShift.add(sliceString.reverse().toString());
        }
        for (int offset = 0; offset < 16; offset++) {
            StringBuilder cwInToOut = new StringBuilder();
            StringBuilder ccwInToOut = new StringBuilder();
            StringBuilder cwOutToIn = new StringBuilder();
            StringBuilder ccwOutToIn = new StringBuilder();
            for (int slice = 0; slice < 16; slice++) {
                cwInToOut.append(inToOutNoShift.get((slice + offset) % 16));
                ccwInToOut.append(inToOutNoShift.get((16 + slice - offset) % 16));
                cwOutToIn.append(outToInNoShift.get((slice + offset) % 16));
                ccwOutToIn.append(outToInNoShift.get((16 + slice - offset) % 16));


            }
            sliceStrings.add(StringUtils.convertBinaryStringToHex(cwInToOut.toString()));
            sliceStrings.add(StringUtils.convertBinaryStringToHex(ccwInToOut.toString()));
            sliceStrings.add(StringUtils.convertBinaryStringToHex(cwOutToIn.toString()));
            sliceStrings.add(StringUtils.convertBinaryStringToHex(ccwOutToIn.toString()));

        }
        return sliceStrings;
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
                spiralStrings.add(StringUtils.convertBinaryStringToHex(generator.generate(coordinates, Ring.segmentOrder)));
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


    private static List<String> buildPossiblePrivateKeys(List<RingStringGenerator> generators) {
        List<String> retVal = Lists.newArrayList();

        retVal.addAll(inToOutTraversal(generators));
        retVal.addAll(spiral(generators));
//        retVal.addAll(subsectionXor(colorMap, thicknessMap));

        return retVal;

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


//                if (count % 1000 == 0 || count < 100) {

                System.out.println(count);
                System.out.println(privateKeys.size());
//                for (String privateKey : privateKeys) {
//                    System.out.println(privateKey);
//                    String ascii = StringUtils.hexToAscii(privateKey);
//                    if (ascii != null)
//                        System.out.println(ascii);
//                }
//                }
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
        for (SegmentType segmentType : Ring.segmentOrder) {
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
