import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Application2 {
    static Map<Color, String> colorMap = Maps.newHashMap();
    static Map<Thickness, String> thicknessMap = Maps.newHashMap();

    static {
        colorMap.put(Color.PINK, "11");
        colorMap.put(Color.DG, "10");
        colorMap.put(Color.BLUE, "01");
        colorMap.put(Color.LG, "00");
        thicknessMap.put(Thickness.S, "00");
        thicknessMap.put(Thickness.M, "01");
        thicknessMap.put(Thickness.L, "10");
        thicknessMap.put(Thickness.XL, "11");
    }

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

    private static String getRingString(int ring) {
        StringBuilder binaryString = new StringBuilder();
        binaryString.append(colorMap.get(segmentProperties[hexOrder[ring * 4]].color)).append(thicknessMap.get(segmentProperties[hexOrder[ring * 4]].thickness));
        binaryString.append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 1]].color)).append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 1]].thickness));
        binaryString.append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 2]].color)).append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 2]].thickness));
        binaryString.append(colorMap.get(segmentProperties[hexOrder[ring * 4 + 3]].color)).append(thicknessMap.get(segmentProperties[hexOrder[ring * 4 + 3]].thickness));
        return binaryString.toString();
    }


    private static List<String> buildPossiblePrivateKeys() {
        List<String> retVal = Lists.newArrayList();
        StringBuilder normal = new StringBuilder();
        StringBuilder leftShiftBuilder = new StringBuilder();
        StringBuilder rightShiftBuilder = new StringBuilder();

        for (int ring = 0; ring < 16; ring++) {
            String ringChar = getRingString(ring);


            String rightShift = rightRotate(ringChar, ring);
            String leftShift = leftRotate(ringChar, ring);
            normal.append(Integer.toString(Integer.parseInt(ringChar,2),16));
            rightShiftBuilder.append(Integer.toString(Integer.parseInt(rightShift,2),16));
            leftShiftBuilder.append(Integer.toString(Integer.parseInt(leftShift,2),16));

        }

        retVal.add(normal.toString());
        retVal.add(leftShiftBuilder.toString());
        retVal.add(rightShiftBuilder.toString());
        retVal.add(normal.reverse().toString());
        retVal.add(leftShiftBuilder.reverse().toString());
        retVal.add(rightShiftBuilder.reverse().toString());
        return retVal;

    }

    public static void main(String[] args) {

        System.out.println(Long.parseLong("000101101010001010100101010",2));
        for (String s : buildPossiblePrivateKeys()) {
            System.out.println(s);
            validate(s);
        }


    }

    private static String leftRotate(String s, int shift) {
        int i = shift % s.length();
        return s.substring(i) + s.substring(0, i);
    }

    private static String rightRotate(String s, int shift){
        int i = s.length() - (shift%s.length());
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
