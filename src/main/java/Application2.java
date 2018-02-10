import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Application2 {

    private static enum Color {
        PINK,
        DG,
        BLUE,
        LG;

    }

    private static enum Thickness {
        S,
        M,
        L,
        XL;
    }


    private static class Segment {
        Color color;
        Thickness thickness;

        public Segment(Color color, Thickness thickness) {
            this.color = color;
            this.thickness = thickness;
        }
    }

    static Segment[] segments = new Segment[]{
            new Segment(Color.DG, Thickness.S), new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.M), new Segment(Color.LG, Thickness.M),
            new Segment(Color.PINK, Thickness.XL), new Segment(Color.LG, Thickness.L), new Segment(Color.DG, Thickness.XL), new Segment(Color.LG, Thickness.XL),
            new Segment(Color.BLUE, Thickness.M), new Segment(Color.DG, Thickness.L), new Segment(Color.BLUE, Thickness.S), new Segment(Color.PINK, Thickness.M),
            new Segment(Color.PINK, Thickness.L), new Segment(Color.PINK, Thickness.L), new Segment(Color.DG, Thickness.M), new Segment(Color.PINK, Thickness.L),
            new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.S), new Segment(Color.PINK, Thickness.M), new Segment(Color.BLUE, Thickness.L),
            new Segment(Color.DG, Thickness.XL), new Segment(Color.BLUE, Thickness.S), new Segment(Color.LG, Thickness.XL), new Segment(Color.LG, Thickness.XL),
            new Segment(Color.BLUE, Thickness.S), new Segment(Color.DG, Thickness.L), new Segment(Color.PINK, Thickness.XL), new Segment(Color.BLUE, Thickness.M),
            new Segment(Color.PINK, Thickness.L), new Segment(Color.BLUE, Thickness.XL), new Segment(Color.PINK, Thickness.M), new Segment(Color.LG, Thickness.S),
            new Segment(Color.LG, Thickness.XL), new Segment(Color.PINK, Thickness.XL), new Segment(Color.BLUE, Thickness.L), new Segment(Color.LG, Thickness.L),
            new Segment(Color.BLUE, Thickness.L), new Segment(Color.BLUE, Thickness.S), new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.M),
            new Segment(Color.BLUE, Thickness.S), new Segment(Color.DG, Thickness.S), new Segment(Color.DG, Thickness.M), new Segment(Color.PINK, Thickness.S),
            new Segment(Color.LG, Thickness.M), new Segment(Color.DG, Thickness.L), new Segment(Color.LG, Thickness.S), new Segment(Color.BLUE, Thickness.XL),
            new Segment(Color.BLUE, Thickness.XL), new Segment(Color.LG, Thickness.L), new Segment(Color.DG, Thickness.L), new Segment(Color.LG, Thickness.S),
            new Segment(Color.LG, Thickness.XL), new Segment(Color.LG, Thickness.S), new Segment(Color.BLUE, Thickness.XL), new Segment(Color.BLUE, Thickness.XL),
            new Segment(Color.PINK, Thickness.M), new Segment(Color.BLUE, Thickness.M), new Segment(Color.PINK, Thickness.L), new Segment(Color.LG, Thickness.M),
            new Segment(Color.DG, Thickness.L), new Segment(Color.BLUE, Thickness.L), new Segment(Color.PINK, Thickness.S), new Segment(Color.DG, Thickness.L),
    };

    static Segment[] segmentProperties = new Segment[]{
            new Segment(Color.DG, Thickness.S),
            new Segment(Color.LG, Thickness.S),
            new Segment(Color.PINK, Thickness.S),
            new Segment(Color.BLUE, Thickness.S),
            new Segment(Color.DG, Thickness.M),
            new Segment(Color.LG, Thickness.M),
            new Segment(Color.PINK, Thickness.M),
            new Segment(Color.BLUE, Thickness.M),
            new Segment(Color.DG, Thickness.L),
            new Segment(Color.LG, Thickness.L),
            new Segment(Color.PINK, Thickness.L),
            new Segment(Color.BLUE, Thickness.L),
            new Segment(Color.DG, Thickness.XL),
            new Segment(Color.LG, Thickness.XL),
            new Segment(Color.PINK, Thickness.XL),
            new Segment(Color.BLUE, Thickness.XL),

    };

    static int[] hexOrder = new int[]{0, 5, 4, 5, 14, 9, 12, 13, 7, 8, 3, 6, 10, 10, 4, 10, 5, 0, 6, 11, 12, 3, 13, 13, 3, 8, 14, 7, 10, 15, 6, 1, 13, 14, 11, 9, 11, 3, 5, 4, 3, 0, 4, 2, 5, 8, 1, 15, 15, 9, 8, 1, 13, 1, 15, 15, 6, 7, 10, 5, 8, 11, 2, 8};

    private static class CTCTRingStringGenerator implements RingStringGenerator {
        public String generate(int ring, Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
            StringBuilder binaryString = new StringBuilder();
            binaryString
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 1]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 1]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 2]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 2]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 3]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 3]].thickness));
            return binaryString.toString();
        }
    }

    private static class TCTCRingStringGenerator implements RingStringGenerator {
        public String generate(int ring, Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
            StringBuilder binaryString = new StringBuilder();
            binaryString
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 1]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 1]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 2]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 2]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 3]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 3]].color));
            return binaryString.toString();
        }
    }

    private static class CCTTRingStringGenerator implements RingStringGenerator {
        public String generate(int ring, Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
            StringBuilder binaryString = new StringBuilder();
            binaryString
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 1]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 2]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 3]].color))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 1]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 2]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 3]].thickness));
            return binaryString.toString();
        }
    }


    private static class TTCCRingStringGenerator implements RingStringGenerator {
        public String generate(int ring, Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
            StringBuilder binaryString = new StringBuilder();
            binaryString
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 1]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 2]].thickness))
                    .append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 3]].thickness))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 1]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 2]].color))
                    .append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 3]].color));
            return binaryString.toString();
        }
    }


    private static interface RingStringGenerator {
        String generate(int ring, Map<Color, String> colorMap, Map<Thickness, String> thicknessMap);
    }

    private static List<RingStringGenerator> generators = Lists.newArrayList(new CCTTRingStringGenerator(), new CTCTRingStringGenerator(), new TCTCRingStringGenerator(), new TTCCRingStringGenerator());


    private static List<String> buildPossiblePrivateKeys(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
        List<String> retVal = Lists.newArrayList();
        List<StringBuilder> builders = Lists.newArrayList();


        for (int genIndex = 0; genIndex < generators.size(); genIndex++) {
            RingStringGenerator generator = generators.get(genIndex);
            StringBuilder normal = new StringBuilder();
            StringBuilder leftShiftBuilder = new StringBuilder();
            StringBuilder rightShiftBuilder = new StringBuilder();
            StringBuilder normalPrepend = new StringBuilder();
            StringBuilder leftShiftPrependBuilder = new StringBuilder();
            StringBuilder rightShiftPrependBuilder = new StringBuilder();
            for (int ring = 0; ring < 16; ring++) {


                String ringChar = generator.generate(ring, colorMap, thicknessMap);

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

    public static String convertBinaryStringToHex(String s) {
        String s1 = Integer.toString(Integer.parseInt(s, 2), 16);
        while (s1.length() < 4)
            s1 = "0" + s1;
        return s1;
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
